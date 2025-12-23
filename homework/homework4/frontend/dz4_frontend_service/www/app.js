const logEl = document.getElementById('log');
const ordersOut = document.getElementById('ordersOut');

function userId() {
  const v = document.getElementById('userId').value.trim();
  if (!v) throw new Error('User ID пустой');
  return v;
}

function log(msg, obj) {
  const time = new Date().toISOString().replace('T', ' ').replace('Z','');
  let line = `[${time}] ${msg}`;
  if (obj !== undefined) {
    try { line += `\n${JSON.stringify(obj, null, 2)}`; }
    catch { line += `\n${String(obj)}`; }
  }
  logEl.textContent = line + "\n\n" + logEl.textContent;
}

async function api(method, url, body) {
  const headers = { 'X-User-Id': userId() };
  const opts = { method, headers };

  if (body !== undefined) {
    headers['Content-Type'] = 'application/json';
    opts.body = JSON.stringify(body);
  }

  const res = await fetch(url, opts);
  const text = await res.text();
  let data;
  try { data = text ? JSON.parse(text) : null; } catch { data = text; }

  if (!res.ok) {
    throw new Error(`${method} ${url} -> ${res.status} ${res.statusText}\n${typeof data === 'string' ? data : JSON.stringify(data)}`);
  }
  return data;
}

function renderOrders(list) {
  if (!Array.isArray(list)) {
    ordersOut.textContent = JSON.stringify(list, null, 2);
    return;
  }
  ordersOut.textContent = list
    .map(o => `${o.id} | ${o.status} | amount=${o.amount} | ${o.description}`)
    .join('\n');
}

async function health() {
  const out = document.getElementById('healthOut');
  try {
    const [o, p] = await Promise.all([
      api('GET', '/api/orders/health'),
      api('GET', '/api/payments/health'),
    ]);
    out.textContent = `orders=${o.status}; payments=${p.status}`;
    log('health ok', { orders: o, payments: p });
  } catch (e) {
    out.textContent = 'ERROR';
    log('health failed', String(e));
  }
}

async function createAccount() {
  const r = await api('POST', '/api/payments/accounts', {});
  log('account created/exists', r);
}

async function topup() {
  const amount = Number(document.getElementById('topupAmount').value);
  const r = await api('POST', '/api/payments/accounts/topup', { amount });
  log('topped up', r);
}

async function balance() {
  const r = await api('GET', '/api/payments/accounts/balance');
  document.getElementById('balanceOut').textContent = String(r.balance ?? '');
  log('balance', r);
}

async function listOrders() {
  const r = await api('GET', '/api/orders/orders');
  renderOrders(r);
  log('orders list', r);
}

async function getOrder() {
  const id = document.getElementById('orderId').value.trim();
  if (!id) throw new Error('orderId пустой');
  const r = await api('GET', `/api/orders/orders/${encodeURIComponent(id)}`);
  log('order', r);
  return r;
}

async function pollStatus(orderId) {
  for (let i = 0; i < 60; i++) {
    const r = await api('GET', `/api/orders/orders/${encodeURIComponent(orderId)}`);
    log(`poll ${i + 1}: ${r.status}`, r);
    renderOrders([r]);
    if (r.status && r.status !== 'NEW') return r;
    await new Promise(res => setTimeout(res, 1000));
  }
  throw new Error('poll timeout: статус не изменился за 60 сек');
}

async function createOrder() {
  const amount = Number(document.getElementById('orderAmount').value);
  const description = document.getElementById('orderDesc').value;
  const r = await api('POST', '/api/orders/orders', { amount, description });
  log('order created', r);
  document.getElementById('orderId').value = r.id || '';
  if (document.getElementById('autoPoll').checked && r.id) {
    try {
      await pollStatus(r.id);
    } catch (e) {
      log('auto poll failed', String(e));
    }
  }
}

document.getElementById('btnHealth').addEventListener('click', () => health());
document.getElementById('btnCreateAccount').addEventListener('click', () => createAccount().catch(e => log('ERROR', String(e))));
document.getElementById('btnTopup').addEventListener('click', () => topup().catch(e => log('ERROR', String(e))));
document.getElementById('btnBalance').addEventListener('click', () => balance().catch(e => log('ERROR', String(e))));
document.getElementById('btnCreateOrder').addEventListener('click', () => createOrder().catch(e => log('ERROR', String(e))));
document.getElementById('btnListOrders').addEventListener('click', () => listOrders().catch(e => log('ERROR', String(e))));
document.getElementById('btnGetOrder').addEventListener('click', () => getOrder().then(r => renderOrders([r])).catch(e => log('ERROR', String(e))));
document.getElementById('btnClear').addEventListener('click', () => { logEl.textContent = ''; ordersOut.textContent = ''; });

health();

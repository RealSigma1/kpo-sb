FROM nginx:1.27-alpine

# Nginx config with reverse-proxy to backend services
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Static frontend
COPY www/ /usr/share/nginx/html/

EXPOSE 80

# Basic container healthcheck (Nginx is up)
HEALTHCHECK --interval=10s --timeout=3s --retries=10 \
  CMD wget -q -O - http://localhost/ >/dev/null 2>&1 || exit 1

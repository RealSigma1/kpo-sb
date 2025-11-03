package financialaccounting.Interfaces;

import financialaccounting.Models.Operation;
import java.util.Date;
import java.util.List;

public interface IOperationRepository {
    void add(Operation entity);
    void update(Operation entity);
    void delete(int id);
    Operation getById(int id);
    List<Operation> getAll();
    boolean exists(int id);
    List<Operation> getByAccountId(int accountId);
    List<Operation> getByPeriod(Date startDate, Date endDate);
    List<Operation> getByCategoryId(int categoryId);
}
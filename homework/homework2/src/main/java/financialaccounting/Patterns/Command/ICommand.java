package financialaccounting.Patterns.Command;

public interface ICommand {
    void execute();
    void undo();
}
package financialaccounting.Patterns.Command;

public class CommandTimerDecorator implements ICommand {
    private ICommand decoratedCommand;

    public CommandTimerDecorator(ICommand decoratedCommand) {
        this.decoratedCommand = decoratedCommand;
    }

    @Override
    public void execute() {
        long startTime = System.currentTimeMillis();
        decoratedCommand.execute();
        long endTime = System.currentTimeMillis();
        System.out.println("Время выполнения: " + (endTime - startTime) + " мс");
    }

    @Override
    public void undo() {
        long startTime = System.currentTimeMillis();
        decoratedCommand.undo();
        long endTime = System.currentTimeMillis();
        System.out.println("Время отмены: " + (endTime - startTime) + " мс");
    }
}
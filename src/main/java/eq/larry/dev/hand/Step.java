package eq.larry.dev.hand;

import org.bukkit.ChatColor;

public enum Step {
    LOBBY(true, ChatColor.AQUA + "Rejoindre"),
    IN_GAME(false, ChatColor.RED + "En jeu"),
    POST_GAME(false, ChatColor.DARK_RED + "Victoire");

    private static Step currentStep;

    private boolean canJoin;

    private String motd;

    public static boolean canJoin() {
        return currentStep.canJoin;
    }

    public static String getMOTD() {
        return currentStep.motd;
    }

    public static boolean isStep(Step step) {
        return (currentStep == step);
    }

    public static void setCurrentStep(Step currentStep) {
        Step.currentStep = currentStep;
    }

    public static Step getCurrentStep() {
        return currentStep;
    }

    public void setCanJoin(boolean canJoin) {
        this.canJoin = canJoin;
    }

    Step(boolean canJoin, String motd) {
        this.canJoin = canJoin;
        this.motd = motd;
    }
}

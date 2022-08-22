package data;

public final class Repositories {
    private static Repositories repo = new Repositories();

    private Repositories() {
    }

    public static Repositories getInstance() {
        return repo;
    }


    public UserRepository getUserRepository() {
        return new UserRepository();
    }

    public HardcodedRepository getHardcodedRepository() {
        return HardcodedRepository.getInstance();
    }

}

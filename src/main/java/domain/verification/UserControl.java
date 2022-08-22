package domain.verification;

public class UserControl {
    private String username;
    private String password;
    private String confirmPass;
    private String email;

    public UserControl(String username, String password, String confirmPass, String email) {
        this.username = username;
        this.password = password;
        this.confirmPass = confirmPass;
        this.email = email;
    }

    public boolean passwordsMatch() {
        return getPassword().equals(getConfirmPass());
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getConfirmPass() {
        return confirmPass;
    }

    public String getEmail() {
        return email;
    }
}

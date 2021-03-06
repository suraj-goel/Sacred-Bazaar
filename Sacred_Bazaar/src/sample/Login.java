package sample;
//importing jdbc libraries
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Login implements Serializable {

    //Instance variable having usual names
    private String email,password;

    //public constructor for initializing instance variable
    public Login(LoginRequest user){
        this.email=user.getEmail();
        this.password=user.getPassword();
    }

    // method for validating user
    public User check(){
        String query="SELECT * from users where Email='"+email+"';";
        User user= new User();;
        try {
            PreparedStatement stmt=Main.connection.prepareStatement(query);
            ResultSet rs=stmt.executeQuery();
            while (rs.next()){
                System.out.println(rs.getString(6));
                System.out.println((this.password));

                if((this.password).equals(rs.getString(6))){
                    String query1="Select * from AmountSpendUser where BonusEndDate is NOT NULL && UserID=?;";
                    PreparedStatement preparedStatement=Main.connection.prepareStatement(query1);
                    preparedStatement.setString(1,rs.getString(1));
                    ResultSet resultSet=preparedStatement.executeQuery();
                    while (resultSet.next()){
                        user.setBonusOffer(true);
                    }
                    String query2="Select * from AmountSpendUser where DiscountEndDate is NOT NULL && UserID=?;";
                    preparedStatement=Main.connection.prepareStatement(query2);
                    preparedStatement.setString(1,rs.getString(1));
                    resultSet=preparedStatement.executeQuery();
                    while (resultSet.next()){
                        user.setExtraOffer(true);
                    }
                    user.setId(rs.getString(1));
                    user.setEmail(rs.getString(2));
                    user.setFirstName(rs.getString(3));
                    user.setLastName(rs.getString(4));
                    user.setRetailerStatus(rs.getInt(7));
                    user.setPhone(rs.getString(5));
                    user.setVerificationStatus(String.valueOf(LoginStatus.VERIFIED));
                    return user;
                }
                user.setVerificationStatus(String.valueOf(LoginStatus.INCORRECT_PASSWORD));
                return user;//returned the string failed
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e);
//            user.setVerificationStatus(String.valueOf(LoginStatus.DOES_NOT_EXIST));
//            return user;
        }
        user = new User();
        user.setVerificationStatus(String.valueOf(LoginStatus.OTHER));
        return user;//returned error while entering data in database
    }

}

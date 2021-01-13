package kz.jihc.technolab.ubttracker.ui.class_menu.models;


import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.Comparator;

@IgnoreExtraProperties
public class Student implements Serializable {
    String info;
    String photo;
    String phoneNumber;
    String imgStorageName;
    String classId;
    String className;
    String email;
    String enterDate;
    String userType;
    int typeRating;
    int point;
    int entAve;
    int ratingInGroups;

    public Student() {

    }

    public Student(String info, String email, String phoneNumber, String classId, String className, String photo, String enterDate, String userType, String imgStorageName, int point, int entAve, int ratingInGroups){
        this.info = info;
        this.classId = classId;
        this.className = className;
        this.phoneNumber = phoneNumber;
        this.photo = photo;
        this.email = email;
        this.imgStorageName = imgStorageName;
        this.point = point;
        this.entAve = entAve;
        this.ratingInGroups = ratingInGroups;
        this.enterDate = enterDate;
        this.userType = userType;
    }

    public Student(int typeRating, String info, String email, String phoneNumber, String classId, String className, String photo, String enterDate, String userType, String imgStorageName, int point, int entAve, int ratingInGroups){
        this.typeRating = typeRating;
        this.info = info;
        this.classId = classId;
        this.className = className;
        this.phoneNumber = phoneNumber;
        this.photo = photo;
        this.email = email;
        this.imgStorageName = imgStorageName;
        this.point = point;
        this.entAve = entAve;
        this.ratingInGroups = ratingInGroups;
        this.enterDate = enterDate;
        this.userType = userType;
    }

    public static Comparator<Student> userNameComprator = new Comparator<Student>() {

        public int compare(Student u1, Student u2) {
            String user1 = u1.getInfo().toUpperCase();
            String user2 = u2.getInfo().toUpperCase();

            //ascending order
            return user1.compareTo(user2);

            //descending order
            //return StudentName2.compareTo(StudentName1);
        }
    };

    /*
    public static Comparator<Student> userReadedBooks = new Comparator<Student>() {

        public int compare(Student u1, Student u2) {
            int user1 = u1.getBookCount();
            int user2 = u2.getBookCount();

            //ascending order
//            return user1.compareTo(user2);

            return user2-user1;

            //descending order
            //return StudentName2.compareTo(StudentName1);
        }
    };
    */

    public static Comparator<Student> userPoint = new Comparator<Student>() {

        public int compare(Student u1, Student u2) {
            int user1 = u1.getPoint();
            int user2 = u2.getPoint();

            //ascending order
//            return user1.compareTo(user2);

            return user2-user1;

            //descending order
            //return StudentName2.compareTo(StudentName1);
        }
    };

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public int getTypeRating() {
        return typeRating;
    }

    public void setTypeRating(int typeRating) {
        this.typeRating = typeRating;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getEnterDate() {
        return enterDate;
    }

    public void setEnterDate(String enterDate) {
        this.enterDate = enterDate;
    }

    public int getRatingInGroups() {
        return ratingInGroups;
    }

    public void setRatingInGroups(int ratingInGroups) {
        this.ratingInGroups = ratingInGroups;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public int getEntAve() {
        return entAve;
    }

    public void setEntAve(int entAve) {
        this.entAve = entAve;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImgStorageName() {
        return imgStorageName;
    }

    public void setImgStorageName(String imgStorageName) {
        this.imgStorageName = imgStorageName;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

}

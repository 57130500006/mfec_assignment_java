package MFEC;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import static java.lang.System.out;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import jdk.nashorn.internal.parser.JSONParser;

public class Assignment {

    public ArrayList<Phone> phoneList = new ArrayList<>();

    public static void main(String[] args)
            throws IOException, ParseException {

        Assignment assignment = new Assignment();
        assignment.callCulculate("promotion1.log"); //เรียกเมธอดคำนวณค่าโทร โดยบอกพาทธ์ของไฟล์ลงไป promotion1.log
        assignment.createJSONFile(); //เรียกเมธอดสร้างไฟล์ JSON โดยตั้งชื่อไฟล์ว่า "promotion1.json"
    }

    public void callCulculate(String strPath)
            throws IOException, ParseException {
        String content;
        String Promotions[];
        Phone phone = null;

        content = new String(Files.readAllBytes(Paths.get(strPath)));
        Promotions = content.split("\\n"); //ตัดข้อมูลทีละบรรทัดก่อน เก็บลงใน Promotions
        for (int i = 0; i < Promotions.length; i++) {
            String getValue[] = Promotions[i].split("\\|"); //เอาข้อมูลที่เป็นแถวมาระบุชนิดของข้อมูล และเก็บลงเป็น object phone ของแต่ละข้อมูลแถวนั้นๆ
            phone = new Phone();
            phone.setPromotionId(getValue[4]);
            phone.setPhoneNumber(getValue[3]);
            phone.setDate(getValue[0]);
            phone.setStartTime(getValue[1]);
            phone.setEndTime(getValue[2]);
            phone.setAmount(0);
            phoneList.add(phone); //เก็บข้อมูลแต่ object phone ลงใน PhoneList 
        }
        calculate(); //เรียกเมธอด calculate เพื่อคำนวณเงินค่าโทร 
    }

    public void calculate() throws ParseException {
        for (int i = 0; i < phoneList.size(); i++) {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            Date start = sdf.parse(phoneList.get(i).startTime);
            Date end = sdf.parse(phoneList.get(i).endTime);
            long millisecond = end.getTime() - start.getTime(); // ลบเวลาโทรตั้งต้นกับเวลาสิ้นสุดการโทร
            long second = TimeUnit.MILLISECONDS.toSeconds(millisecond); //แปลงมิลลิวินาทีเป็นนาที
            int count = 0;
            for (long j = second; j > 0; j = j - 60) {
                ++count; // คำนวณให้วินาทีเป็นนาที
            }
            float amount = (count - 1) + 3; //กำหนดให้นาทีแรกคือ 3 บาทและหลังจากคือ 1 บาททั้งหมด
            phoneList.get(i).setAmount(amount);
            //System.out.println(phoneList.get(i)); //แสดงค่าที่อยู่ใน phoneList ว่ามีข้อมูลไหม
        }
    }

    public void createJSONFile() throws IOException {
        try (Writer writer = new FileWriter("promotion.json")) { //สร้าง json ไฟล์ ชื่อว่า "promotion.json"
            Gson gson = new GsonBuilder().create();
            gson.toJson(phoneList, writer);
            System.out.println("Successfully to created Phones Object to JSON File...");
        }
    }
}

class Phone {

    String date;
    String startTime;
    String endTime;
    String phoneNumber;
    String promotionId;
    float amount;

    public Phone() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(String promotionId) {
        this.promotionId = promotionId;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return promotionId + " " + date + " " + startTime + " " + endTime + " " + phoneNumber + " " + amount + "฿";
    }

}

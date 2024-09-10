package com.nau.dao;

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class adminTest {
    /*@Autowired
    private AdminDao adminDao;
    @Test
    void test(){
        List<Admin> admins = adminDao.selectAll();
        for (Admin admin : admins) {
            System.out.println(admin);
        }
    }
    
    @Test
    void testUUID(){
        System.out.println(getUUID());
        System.out.println(getDate());
        System.out.println(passToMD5("123456"));
    }
    
    public static String getUUID(){
        String str = UUID.randomUUID().toString();
        //str = str.replace("-","");
        return str;
    }
    
    public static Date getDate(){
        //TimeZone time = TimeZone.getTimeZone("ETC/GMT-8");
        TimeZone time = TimeZone.getTimeZone("Asia/Shanghai");
        TimeZone.setDefault(time);
        Date now = new Date();
        return now;
    }
    
    public static String passToMD5(String passWord){
        StringBuilder sb = new StringBuilder();
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] digest = messageDigest.digest(passWord.getBytes());
            for(int  i = 0; i < digest.length; i++){
                int result = digest[i] & 0xff;
                String hexString = Integer.toHexString(result);
                if(hexString.length() < 2){
                    sb.append("0");
                }
                sb.append(hexString);
            }
        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        return sb.toString();
    }*/
}

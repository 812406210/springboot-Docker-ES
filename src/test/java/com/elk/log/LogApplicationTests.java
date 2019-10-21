package com.elk.log;

import com.alibaba.fastjson.JSONObject;
import com.elk.log.model.Person;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LogApplicationTests {

    @Test
    public void contextLoads() {
    }

    @Test
    public void person() {


        StringBuffer sb = new StringBuffer();
        sb.append("111"+",");
        sb.append("222"+",");
        sb.append("333"+",");
        String s=sb.substring(0,sb.length()-1);
        System.out.println(s);
        String str = "1b03c3de7c1b47a9b8b64a98953c7286_release_20120910_exception_handle";
        String str1 = "1b03c3de7c1b47a9b8b64a98953c7286_release_20120910";
        String str3 = "flow_1112553568805_release_20191010";
        str3 = str3.replaceAll("_release_+\\d{8}","");
        str1 = str1.replaceAll("_+\\w+_+\\d{8}","");
        str = str.replaceAll("_+\\w+_+\\d{8}","");
        System.out.println(str);
//        Person p = new Person();
//        p.setAge(12);
//
//        int age = p.getAge();
//        Person person = new Person();
//        person.setName(p.getName());

//        String a = "";
////        String b = null;
////
////        String[] c =a.split(",");
////        System.out.println(c);
////        String[] d =b.split(",");
////        System.out.println(d);
////
////
////        List<Person> personList = new ArrayList<>();
////        personList.add(new Person(1,"1",1));
////        String str = JSONObject.toJSONString(personList);
////        JSONObject.parseObject(str,Person.class);
////        for (Person p:personList) {
////            System.out.println(p);
////        }
////        System.out.println(personList.isEmpty());
    }


}

package com.sz;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.sz.dao.StudentMapper;
import com.sz.pojo.Student;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MybatisPlusDemoApplicationTests {

    @Autowired
    protected StudentMapper studentMapper;

    //查询所有
    @Test
    public void select() {

        List<Student> list = studentMapper.selectList(null);
        list.forEach(System.out::print);
    }

    //条件查询:名字中有张，年龄小于40；
//    DEBUG==>  Preparing: SELECT stuid,stusex,stuage,stuname FROM tblstudent WHERE (stuname LIKE ? AND stuage < ?)
//    DEBUG==> Parameters: %张%(String), 40(Integer)
    @Test
    public void selectByWrapper() {
        QueryWrapper<Student> wrapper = new QueryWrapper<>();
        wrapper.like("stuname", "张").lt("stuage", 40);
        List<Student> list = studentMapper.selectList(wrapper);
        list.forEach(System.out::println);
    }

    //条件查询:年龄在17-19；
//    DEBUG==>  Preparing: SELECT stuid,stusex,stuage,stuname FROM tblstudent WHERE (stuage BETWEEN ? AND ?)
//    DEBUG==> Parameters: 17(Integer), 19(Integer)
    @Test
    public void selectByWrapper2() {
        QueryWrapper<Student> wrapper = new QueryWrapper<>();
        wrapper.between("stuage", 17, 19);
        List<Student> list = studentMapper.selectList(wrapper);
        list.forEach(System.out::println);
    }

    //条件查询:年龄不为空；
//    DEBUG==>  Preparing: SELECT stuid,stusex,stuage,stuname FROM tblstudent WHERE (stuage IS NOT NULL)
    @Test
    public void selectByWrapper3() {
        QueryWrapper<Student> wrapper = new QueryWrapper<>();
        wrapper.isNotNull("stuage");
        List<Student> list = studentMapper.selectList(wrapper);
        list.forEach(System.out::println);
    }

    //条件查询:姓张 或者 年龄>=19，按年龄降序，年龄相同，按id升序；
//    DEBUG==>  Preparing: SELECT stuid,stusex,stuage,stuname FROM tblstudent WHERE (stuname LIKE ? OR stuage >= ?) ORDER BY stuage DESC , stuid ASC
//    DEBUG==> Parameters: 张%(String), 19(Integer)
    @Test
    public void selectByWrapper4() {
        QueryWrapper<Student> wrapper = new QueryWrapper<>();
        wrapper.likeRight("stuname", "张").or().ge("stuage", 19)
                .orderByDesc("stuage").orderByAsc("stuid");
        List<Student> list = studentMapper.selectList(wrapper);
        list.forEach(System.out::println);
    }

    //条件查询:姓张 或者 年龄>=19并且年龄<=21并且性别不为空；只返回第一条记录
//    DEBUG==>  Preparing: SELECT stuid,stusex,stuage,stuname FROM tblstudent WHERE (stuname LIKE ? OR ( (stuage >= ? AND stuage <= ? AND stusex IS NOT NULL) )) limit 1
//    DEBUG==> Parameters: 张%(String), 19(Integer), 21(Integer)
    @Test
    public void selectByWrapper5() {
        QueryWrapper<Student> wrapper = new QueryWrapper<>();
        wrapper.likeRight("stuname", "张")
                .or(stu -> stu.ge("stuage", 19)
                        .le("stuage", 21).isNotNull("stusex")).last("limit 1");
        List<Student> list = studentMapper.selectList(wrapper);
        list.forEach(System.out::println);
    }

    //查询所有，但只要id和name
    //DEBUG==>  Preparing: SELECT stuid,stuname FROM tblstudent
    @Test
    public void select2() {
        QueryWrapper<Student> wrapper = new QueryWrapper<>();
        wrapper.select("stuid", "stuname");
        List<Student> list = studentMapper.selectList(wrapper);
        list.forEach(System.out::print);
    }

    //查询所有，但不要sex和age
    //DEBUG==>  Preparing: SELECT stuid,stuname FROM tblstudent
    @Test
    public void select3() {
        QueryWrapper<Student> wrapper = new QueryWrapper<>();
        wrapper.select(Student.class, stu -> !stu.getColumn().equals("stusex") && !stu.getColumn().equals("stuage"));

        List<Student> list = studentMapper.selectList(wrapper);
        list.forEach(System.out::print);
    }

    //condition:多用于模糊查询,like(boolean condition, R column, Object val)
//    DEBUG==>  Preparing: SELECT stuid,stusex,stuage,stuname FROM tblstudent WHERE (stuname LIKE ?)
//    DEBUG==> Parameters: %张%(String)
    @Test
    public void select4() {
        String stuname = "张";
        String stusex = "";
        QueryWrapper<Student> wrapper = new QueryWrapper<>();
        wrapper.like(StringUtils.isNotEmpty(stuname), "stuname", stuname)
                .like(StringUtils.isNotEmpty(stusex), "stusex", stusex);
        List<Student> list = studentMapper.selectList(wrapper);
        list.forEach(System.out::print);
    }

    /**
     * 参数为实体student ---> new QueryWrapper<>(student);
     * 可以在实体类定义中指定一些属性
     * 例如
     *
     * @TableField(condition = SqlCondition.LIKE)
     * private String stuname;
     * 在sql语句会显示 stuanme like ...
     * 详情见 SqlCondition类
     * <p>
     * %s&lt;&gt;#{%s}
     * %s 表示表中的列  &lt;&gt;表示<>,即为不等于；#{%s}表示传入的值
     * 比如：like("stuname", "张")
     */

    //AllEq
//    DEBUG==>  Preparing: SELECT stusex,stuid,stuage,stuname FROM tblstudent WHERE (stuname = ? AND stuage = ?)
//    DEBUG==> Parameters: 张三丰(String), 18(Integer)
    @Test
    public void testAllEq() {
        QueryWrapper<Student> wrapper = new QueryWrapper<>();
        Map<String, Object> paramas = new HashMap<>();
        paramas.put("stuname", "张三丰");
        paramas.put("stuage", 18);
        wrapper.allEq(paramas);
        List<Student> list = studentMapper.selectList(wrapper);
        list.forEach(System.out::print);
    }

}

package com.c3.vero.c3_5_01_web;

import java.util.List;

/**
 * Created by vero on 2015/12/4.
 */
public interface UserService {
    void UserLogin(String userName,String password)throws Exception;
    void UserRegister(String registerName,String password,List<String> hobbies)throws Exception;
}

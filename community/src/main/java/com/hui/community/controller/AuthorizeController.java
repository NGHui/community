package com.hui.community.controller;

import com.hui.community.dto.AccessTokenDTO;
import com.hui.community.dto.GithubUser;
import com.hui.community.provider.GitHubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author 辉
 * 座右铭:坚持总能遇见更好的自己!
 * @date 2019/10/7
 */
@Controller
public class AuthorizeController {

    @Autowired
    private GitHubProvider gitHubProvider;

    @Value("${github.client.id}")
    private String clientid;
    @Value("${github.client.secret}")
    private String clientsecret;
    @Value("${github.redirect.uri}")
    private String redirecturi;
    /**
     *用于验证GitHub登陆
     * @param code 接收github传过来的code参数
     * @param state
     * @return
     */
    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code")  String code ,
                           @RequestParam(name = "state") String state,
                           HttpSession session, HttpServletRequest request){
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setClient_id(clientid);
        accessTokenDTO.setClient_secret(clientsecret);
        accessTokenDTO.setRedirect_uri(redirecturi);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setState(state);
        String accessToken = gitHubProvider.getAccessToken(accessTokenDTO);
        GithubUser user = gitHubProvider.getUser(accessToken);
        //System.out.println(user);
        if (user!=null){
            user.setName("hui");
            //将user放入session狱中,两种方法
            session.setAttribute("user",user);
            //request.getSession().setAttribute("user",user);
            return "redirect:/";
        }else {
            //登入失败,回到登陆页面
            return "redirect:index";
        }
    }
}

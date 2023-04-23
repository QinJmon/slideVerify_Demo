package com.verify.demo.controller;

import com.anji.captcha.model.common.ResponseModel;
import com.anji.captcha.model.vo.CaptchaVO;
import com.anji.captcha.service.CaptchaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloController {

    @Autowired
    private CaptchaService captchaService;

    @GetMapping("/test")
    @ResponseBody
    public String test(){
        return "hello";
    }

  @PostMapping("getWebToken")
  public ResultBean getWebToken(@RequestBody LoginUser user, String captchaVerification) {
    ResultBean resultBean = new ResultBean();
    CaptchaVO captchaVO = new CaptchaVO();
    captchaVO.setCaptchaVerification(captchaVerification);
    ResponseModel responseModel = captchaService.verification(captchaVO);
    if (!responseModel.isSuccess()) {
      resultBean.fillCode(0, responseModel.getRepMsg());
      return resultBean;
    }
    // 验证通过后，继续登录流程

    return new ResultBean();
  }
}

package com.etjava.filter;

import javax.servlet.http.HttpServletRequest;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

/**
 * zuul 请求过滤
 * @author etjav
 *
 */
public class AccessFilter extends ZuulFilter {

	// 该过滤器是否要被执行
	@Override
	public boolean shouldFilter() {
		// TODO Auto-generated method stub
		return true;
	}

	// 过滤器的具体执行逻辑
	@Override
	public Object run() throws ZuulException {
		RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        String parameter = request.getParameter("accessToken");
        System.out.println(request.getRequestURL().toString()+" 请求访问");
        if(parameter==null) {
        	System.out.println("accessToken为空");
        	ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(401);
            ctx.setResponseBody("{\"result\":\"accessToken is empty!\"}");
        }else {
        	System.out.println(request.getRequestURL().toString()+" 请求成功");
        }
        
        return null;
	}

	// 请求过滤的类型   pre为请求之前，post 表示使用post请求
	@Override
	public String filterType() {
		// TODO Auto-generated method stub
		return "pre";
	}

	// 过滤器的执行顺序
	@Override
	public int filterOrder() {
		return 0;
	}

}

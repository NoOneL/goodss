package goodss.cart.web.servlet;



import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import goodss.book.domain.Book;
import goodss.cart.domain.CartItem;
import goodss.cart.service.CartItemService;
import goodss.user.domain.User;
import cn.itcast.commons.CommonUtils;
import cn.itcast.servlet.BaseServlet;

public class CartItemServlet extends BaseServlet {
	private CartItemService cartItemService=new CartItemService();

	/**
	 * 加载多个CartItem
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String loadCartItems(HttpServletRequest req,HttpServletResponse resp)
		throws ServletException,IOException{
		String cartItemIds=req.getParameter("cartItemIds");
		double total=Double.parseDouble(req.getParameter("total"));
		
		List<CartItem> cartItemList=cartItemService.loadCartItems(cartItemIds);
		
		req.setAttribute("cartItemList", cartItemList);
		req.setAttribute("total", total);
		req.setAttribute("cartItemIds", cartItemIds);
		return "f:/jsps/cart/showitem.jsp";
	}
	
	/**
	 * 修改条目
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String updateQuantity(HttpServletRequest req,HttpServletResponse resp)
		throws ServletException,IOException{
		String cartItemId=req.getParameter("cartItemId");
		int quantity=Integer.parseInt(req.getParameter("quantity"));
		CartItem cartItem=cartItemService.updateQuantity(cartItemId, quantity);
		
		//给客户端返回一饿json对象
		StringBuilder sb=new StringBuilder("{");
		sb.append("\"quantity\"").append(":").append(cartItem.getQuantity());
		sb.append(",");
		sb.append("\"subtotal\"").append(":").append(cartItem.getSubtotal());
		sb.append("}");
		
		resp.getWriter().print(sb);
		return null;
	}
	
	/**
	 * 批量删除
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String batchDelete(HttpServletRequest req,HttpServletResponse resp)
		throws ServletException,IOException{
		/**
		 * 1,获取cartItemIds参数
		 * 调用service方法完成工作
		 * 返回list.jsp
		 */
		String cartItemIds=req.getParameter("cartItemIds");
		cartItemService.batchDelete(cartItemIds);
		return myCart(req, resp);
	}
	
	/**
	 * 添加购物车条目
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String add(HttpServletRequest req,HttpServletResponse resp)
		throws ServletException,IOException{
		Map map=req.getParameterMap();
		CartItem cartItem=CommonUtils.toBean(map, CartItem.class);
		Book book=CommonUtils.toBean(map, Book.class);
		User user=(User)req.getSession().getAttribute("sessionUser");
		cartItem.setBook(book);
		cartItem.setUser(user);
		
		cartItemService.add(cartItem);
		return myCart(req, resp);
	}
	
	/**
	 * 我的购物车
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String myCart(HttpServletRequest req,HttpServletResponse resp)
		throws ServletException,IOException{
		//得到uid
		User user=(User)req.getSession().getAttribute("sessionUser");
		String uid=user.getUid();
		//通过service得到当前用户的所有购物车条目
		List<CartItem> cartItemLIst=cartItemService.myCart(uid);
		//保存
		req.setAttribute("cartItemList", cartItemLIst);
		return "f:/jsps/cart/list.jsp";
	}
}

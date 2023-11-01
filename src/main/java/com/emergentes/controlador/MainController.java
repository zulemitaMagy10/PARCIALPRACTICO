package com.emergentes.controlador;
import com.emergentes.modelo.Producto;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "MainController", urlPatterns = {"/MainController"})
public class MainController extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Verificar si la colección de personas ya existe en el obj session
        HttpSession ses = request.getSession();
        int id,pos;
        
        if (ses.getAttribute("listaper") == null){
            // Crear la coleccion y almacenarla en el objeto session
            ArrayList<Producto> listaux = new ArrayList<Producto>();
            // Colocar listaux como atributo de session
            ses.setAttribute("listaper", listaux);
        }
        
        ArrayList<Producto> lista = (ArrayList<Producto>)ses.getAttribute("listaper");
        
        String op =  request.getParameter("op");
        String opcion = (op != null) ? op : "view";
        
        Producto obj1 = new Producto();
        
        switch (opcion){
            case "nuevo":
                request.setAttribute("miProducto", obj1);
                request.getRequestDispatcher("editar.jsp").forward(request, response);
                break;
            case "editar":
                id = Integer.parseInt(request.getParameter("id"));
                pos = buscarIndice(request,id);
                obj1 = lista.get(pos);
                request.setAttribute("miProducto", obj1);
                request.getRequestDispatcher("editar.jsp").forward(request, response);
                break;
            case "eliminar":
                id = Integer.parseInt(request.getParameter("id"));  
                pos = buscarIndice(request,id);
                lista.remove(pos);
                response.sendRedirect("index.jsp");
                break;
            case "view":
                // ses.setAttribute("listaper", lista);
                response.sendRedirect("index.jsp");
                break;
        }
        

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession ses = request.getSession();
        ArrayList<Producto> lista = (ArrayList<Producto>) ses.getAttribute("listaper");
        Producto obj1 = new Producto();
        int idt; 
        
        obj1.setId(Integer.parseInt(request.getParameter("id")));
        obj1.setDescripcion(request.getParameter("descripcion"));
        obj1.setCantidad(Integer.parseInt(request.getParameter("cantidad")));
        obj1.setPrecio(Double.parseDouble(request.getParameter("precio")));
        obj1.setCategoria(request.getParameter("categoria"));
       
        
        idt = obj1.getId();
        if (idt == 0){
            obj1.setId(ultimoID(request));
            lista.add(obj1);
        }
        else{
            lista.set(buscarIndice(request,idt), obj1);
        }
        response.sendRedirect("index.jsp");
    }
    
    private int ultimoID(HttpServletRequest request){
        HttpSession ses = request.getSession();
        ArrayList<Producto> lista = (ArrayList<Producto>)ses.getAttribute("listaper");
        
        int idaux = 0;
        for (Producto item:lista){
            idaux = item.getId();
        }
        return idaux + 1;
    }
    
    private int buscarIndice(HttpServletRequest request, int id)
    {
        HttpSession ses = request.getSession();
        ArrayList<Producto> lista = (ArrayList<Producto>)ses.getAttribute("listaper");
        
        int i = 0;
        if (lista.size()>0){
            while (i < lista.size()){
                if (lista.get(i).getId() == id ){
                    break;
                }
                else{
                    i++;
                }
            }
        }
        return i;
    }
            
}

package com.arandu.mojo_scorer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gson.Gson;

/**
 * Servlet implementation class ModelScorerServlet
 */
public class ModelScorerService extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Log log = LogFactory.getLog(ModelScorerService.class);
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ModelScorerService() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@SuppressWarnings("unchecked")
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String respuesta="{}";
		log.info("RequestURI:"+request.getRequestURI());
		String jsonParameters=(new Gson()).toJson(request.getParameterMap()).replaceAll("\\[|\\]", "");
		log.info("Parameters:" + jsonParameters);
				
		String path=getServletContext().getRealPath("../")+request.getRequestURI()+".zip";
		
		String s[]=request.getRequestURI().split("/");		
		String model=s[s.length-1];		
		String filePath="/opt/tomcat6/webapps/mojo_scorer/model/"+model+".zip";
				
		File f = new File(path);		
		if(f.isFile()) {
			
			if( !(new File(filePath)).isFile() ) {
				copyFile(path,filePath);
			}
			
			ModelScorer scorer = (ModelScorer) getServletContext().getAttribute(path);
			if(scorer == null) {
				scorer = new ModelScorer(path);
				getServletContext().setAttribute(path, scorer);
			}					
			String queryString = request.getQueryString();
			if(queryString!=null) System.out.println(request.getQueryString());
			
			Map<String, String[]> parameters = new HashMap<String, String[]>();		
			parameters.putAll(request.getParameterMap());
		
			respuesta = jsonParameters.replace("}",",") + scorer.predict(parameters).substring(1);						
		}
		
		log.info("Response:"+respuesta);
		response.setContentType("application/json");		
		response.getWriter().append(respuesta);
				
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		service(request, response);
	}
	
		
	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		service(request, response);
	}


	/**
	 * @see HttpServlet#doPut(HttpServletRequest, HttpServletResponse)
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}
	
	/** 
	 * @param source
	 * @param dest
	 * @throws IOException
	 */
	private void copyFile(String source, String dest) throws IOException {
	    FileChannel sourceChannel = null;
	    FileChannel destChannel = null;
	    try {
	        sourceChannel = new FileInputStream(source).getChannel();
	        destChannel = new FileOutputStream(dest).getChannel();
	        destChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
	       }finally{
	           sourceChannel.close();
	           destChannel.close();
	   }
	}	

}

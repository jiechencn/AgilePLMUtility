package com.agile.support;


public class BaseDecode {
	public static void main(String args[]){
		try{

		String b2 = "e0FFUzoxMjh9QTNENjUyMkZGQ0ZBM0RFOUI5NEUzQjkzOUY3MDY2NURENDdCOEVCMDM4MzJDNkU3REY2RjQ4NzFERUE2QzEwNTIzRERDNTJBNTBFNUY1M0IzMjNDMTAxNzkwMkNBNkU4MzgyNjI1QkE2NDc5REM1MTk3RjQwN0VEMjE3MjU2OUZGNUU0";
		System.out.println(new String(AgileBase64.decode(b2.getBytes())));
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}

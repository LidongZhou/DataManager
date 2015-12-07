package com.dm.datamanager;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class ExcleJXLUtil {
	public static WritableFont arial14font = null;

	public static WritableCellFormat arial14format = null;
	public static WritableFont arial10font = null;
	public static WritableCellFormat arial10format = null;
	public static WritableFont arial12font = null;
	public static WritableCellFormat arial12format = null;

	public final static String UTF8_ENCODING = "UTF-8";
	public final static String GBK_ENCODING = "GBK";

	public static int index = 1;// д�����
	public static int row = 2;// �����ֶ�д��ӵڶ��п�ʼ

	private static int mprogress = 0;
	/**
	 * ��ʽ����
	 */
	public static void format() {
		try {
			arial14font = new WritableFont(WritableFont.ARIAL, 14,
					WritableFont.BOLD);
			arial14font.setColour(jxl.format.Colour.LIGHT_BLUE);
			arial14format = new WritableCellFormat(arial14font);
			arial14format.setAlignment(jxl.format.Alignment.CENTRE);
			arial14format.setBorder(jxl.format.Border.ALL,
					jxl.format.BorderLineStyle.THIN);
			arial14format.setBackground(jxl.format.Colour.VERY_LIGHT_YELLOW);
			arial10font = new WritableFont(WritableFont.ARIAL, 10,
					WritableFont.BOLD);
			arial10format = new WritableCellFormat(arial10font);
			arial10format.setAlignment(jxl.format.Alignment.CENTRE);
			arial10format.setBorder(jxl.format.Border.ALL,
					jxl.format.BorderLineStyle.THIN);
			arial10format.setBackground(jxl.format.Colour.LIGHT_BLUE);
			arial12font = new WritableFont(WritableFont.ARIAL, 12);
			arial12format = new WritableCellFormat(arial12font);
			arial12format.setBorder(jxl.format.Border.ALL,
					jxl.format.BorderLineStyle.THIN);
		} catch (WriteException e) {
			
			e.printStackTrace();
		}
	}
	
	public static void initExcel(String fileName, String[] colName,
			int[] widthArr) {
		ExcleJXLUtil.index = 0;// ����Ϊ��ʼֵ����Ȼstatic��index��һֱ����
		ExcleJXLUtil.row = 2;
		format();// �����ø�ʽ
		WritableWorkbook workbook = null;
		try {
			// WorkbookSettings setEncode = new WorkbookSettings(); // ���ö��ļ�����
			// setEncode.setEncoding(UTF8_ENCODING);
			File file = new File(fileName);
			if(file.exists()){
				file.delete();
				file.createNewFile();
			}else{
				file.createNewFile();		
			}

			workbook = Workbook.createWorkbook(file);

			WritableSheet sheet = workbook.createSheet("Sheet 1", 0);// ����sheet
			sheet.mergeCells(0, 0, colName.length - 1, 0);
			sheet.addCell(new Label(0, 0, fileName,
					arial14format));// ��ͷ�������
			for (int i = 0; i < widthArr.length; i++) {
				sheet.setColumnView(i, widthArr[i]);// ����col ���
			}

			int row = 1;
			int col = 0;
			for (col = 0; col < colName.length; col++) {
				sheet.addCell(new Label(col, row, colName[col], arial10format));// д��
																				// col����
			}
			workbook.write();// д������
		} catch (RowsExceededException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (workbook != null) {
				try {
					workbook.close();
				} catch (WriteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

	/**
	 * �� ����д�뵽excel��
	 * 
	 * @param os
	 *            ����Excel�������
	 * @param objList
	 *            Ҫ���������
	 * @param fieldArr
	 *            �ֶ�����
	 * @param fileName
	 *            excel��ͷ����
	 * @param colName
	 *            excel����
	 * @param widthArr
	 *            excel��Ԫ����
	 */
	
	public static <T> void writeToExcel(SQLiteDatabase database,
			String fileName, String[] fieldArr) {
		if (database != null ) {
			WritableWorkbook writebook = null;
			InputStream in = null;
			try {
				/**
				 * ��ȡԭ��д����ļ�
				 */
				// WorkbookSettings setEncode = new WorkbookSettings();
				// //���ö��ļ�����
				// setEncode.setEncoding(UTF8_ENCODING);
				in = new FileInputStream(new File(fileName));
				Workbook workbook = Workbook.getWorkbook(in);
				writebook = Workbook.createWorkbook(new File(fileName),
						workbook);
				WritableSheet sheet = writebook.getSheet(0);
				/**
				 * д������
				 */
		
				Cursor cursor = database.rawQuery("select * from customer", null);
	            while (cursor.moveToNext()) {
	            	int col = 0;
					index++;
					String serialNumberStr = String.valueOf(index);
					sheet.addCell(new Label(col, row, serialNumberStr,
							arial12format));// ��һ������д���
						col++;	            	    		   
					for (int i = 1; i <= fieldArr.length; i++) {

						String str = cursor.getString(i);
						if (str == null) {
							str = "";
						} 					
						sheet.addCell(new Label(col, row, str, arial12format));
						col++;
					}
	            		            
	            	row++;
	            }
				writebook.write();
				//Toast.makeText(c, "�����ɹ�", 1000).show();
			} catch (BiffException e) {
				e.printStackTrace();
			} catch (IOException e) {
			} catch (RowsExceededException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (WriteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				if (writebook != null) {
					try {
						writebook.close();
					} catch (WriteException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}if(in != null){
					try {
						in.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

		}
	}
	
	
	
	  public static void readToExcel(SQLiteDatabase database,
				String fileName, String[] fieldArr) {  
	        try {  
	  
	            /** 
	             * ������������,����Excel�����ͼƬ�Լ������������͵Ķ�ȡ 
	             **/  
	            InputStream is = new FileInputStream(new File(fileName));  
	  
	            Workbook book = Workbook  
	                    .getWorkbook(new File(fileName));  
	            book.getNumberOfSheets();  
	            // ��õ�һ�����������  
	            Sheet sheet = book.getSheet(0);  
	            int Rows = sheet.getRows();  
	            int Cols = sheet.getColumns();  
	            System.out.println("��ǰ�����������:" + sheet.getName());  
	            System.out.println("������:" + Rows);  
	            System.out.println("������:" + Cols);
	        
	            if(Rows <=2)
	            	mprogress = -1;
	            for (int i = 2; i < Rows; ++i) {  	            	
	                    // getCell(Col,Row)��õ�Ԫ���ֵ  
	        			ContentValues values = new ContentValues();
	        			values.put("shipownername", (sheet.getCell(1, i)).getContents());
	        			values.put("shipownerphone", (sheet.getCell(2, i)).getContents());
	        			values.put("shipname", (sheet.getCell(3, i)).getContents());
	        			values.put("shiplength", (sheet.getCell(4, i)).getContents());
	        			values.put("shipwidth", (sheet.getCell(5, i)).getContents());
	        			values.put("shipheigth", (sheet.getCell(6, i)).getContents());
	        			values.put("shipweight", (sheet.getCell(7, i)).getContents());
	        			values.put("haulheight", (sheet.getCell(8, i)).getContents());
	        			values.put("more", (sheet.getCell(9, i)).getContents());   
	        			database.insert("customer", null, values);
	        			mprogress =   ((i-1)*100)/(Rows-2);             	             
	            }  
	            // �õ���һ�е�һ�еĵ�Ԫ��  
	            
	            Cell cell1 = sheet.getCell(0, 0);  
	            String result = cell1.getContents();  
	            System.out.println(result);  
	            book.close();  
	        } catch (Exception e) {  
	            System.out.println(e);  
	        }  
	    }  
	  
	  	public static int getProgress(){
	  	
	  		if(mprogress==100){
	  	
	  			mprogress = 0;
	  			return 100;
	  		}else if(mprogress ==-1){
	  			mprogress = 0;
	  			return -1;
	  		}else{
	  			return mprogress;
	  		}
	  	}

}

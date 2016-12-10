package optimization;
import ilog.opl.IloOplElement;
import ilog.opl.IloOplModel;


public class ModelAccess {

	/**
	 * get Values from solution parameters
	 */
	public static int getValue(IloOplModel opl_model, String name){
		try{
			IloOplElement el= opl_model.getElement(name);
			int i= el.asInt();
			return i;
		}catch(Exception e){
			e.printStackTrace();
		}
		return -1;
	}
	public static int[] getArray(IloOplModel opl_model, String name){
		int[] array = null;
		 try {	
			IloOplElement el= opl_model.getElement(name);	
			int dim1=el.asIntMap().getSize();
			array = new int[dim1];
			for(int i = 1; i<=dim1; i++){
					array[i-1]= el.asIntMap().get(i);
			 }
		} catch (Exception e1) {
			System.err.println(name +" is not a 1-dim IntMap");
			e1.printStackTrace();
			
		}
		return array;
	}
	
	public static int[][] getArray2(IloOplModel opl_model, String name){
		int[][] map = null;
		try{
			IloOplElement el= opl_model.getElement(name);
			int dim1=el.asIntMap().getSize();
			int dim2=el.asIntMap().getSub(1).getSize();
			
			map = new int[dim1][dim2];
			for(int i = 1; i<=dim1; i++){
				for(int j= 1; j<=dim2; j++){
					map[i-1][j-1]= el.asIntMap().getSub(i).get(j);
				}
			}
		} catch (Exception e1) {
			System.err.println(name +" is not a 2-dim IntMap");
			e1.printStackTrace();
			
		}
		return map;
	}
	
	public static int[][][] getArray3(IloOplModel opl_model, String name){
		int[][][] map = null;
		try{
			IloOplElement el= opl_model.getElement(name);
			int dim1=el.asIntMap().getSize();
			int dim2=el.asIntMap().getSub(1).getSize();
			int dim3=el.asIntMap().getSub(1).getSub(1).getSize();
			map = new int[dim1][dim2][dim3];
			
			for(int i = 1; i<=dim1; i++){	
				for(int j= 1; j<=dim2; j++){
					for(int k= 1; k<=dim3; k++){
						map[i-1][j-1][k-1]= el.asIntMap().getSub(i).getSub(j).get(k);
					}
				}
			}
		} catch (Exception e1) {
			System.err.println(name +" is not a 3-dim IntMap");
			e1.printStackTrace();
			
		}
		return map;
	}
	
	

	//It did not work to insert data directly into the model. Therefore use method via file write and read above
	
//	public static void set(final IloOplFactory fac, final IloOplModel opl_model, final String name, final int value){
//		try{
//			IloOplFactory f = fac;//IloOplFactory.getOplFactoryFrom(opl_model);
//			IloCustomOplDataSource ds = new IloCustomOplDataSource(f) {
//				
//				@Override
//				public void customRead() {
//					try{
//						IloOplDataHandler handler = getDataHandler();
//						handler.startElement(name);
//			
//						//old code
//						IloOplElement el= opl_model.getElement(name);
//						el.asIntMap().set(1, value);				//not sure if this set works
//						//old code end
//								
//						handler.setElement(el);
//						handler.endElement();
//					} catch (Exception e1) {
//						e1.printStackTrace();
//					}
//				}
//			};		
//			//ds.customRead();			
//								
//		} catch (Exception e1) {
//			e1.printStackTrace();
//			
//		}
//	}
//	
//	public static void set(final IloOplFactory fac, final IloOplModel opl_model, final String name, final int[] value){
//		try{
//			
//
//			IloOplFactory f = fac;// IloOplFactory.getOplFactoryFrom(opl_model);
//			IloCustomOplDataSource ds = new IloCustomOplDataSource(f) {
//				
//				@Override
//				public void customRead() {
//					try{
//						IloOplDataHandler handler = getDataHandler();
//						handler.startElement(name);
//			
//						//old code
//						IloOplElement el= opl_model.getElement(name);
//						int dim1=el.asIntMap().getSize();
//						
//						for(int i = 1; i<=dim1; i++){
//							int v=0;
//							if(i<=value.length){		//write zero if model vector is longer than value vector
//								v=value[i-1];
//							}
//							el.asIntMap().set(i, v);
//						}	
//						//old code end
//						
//						handler.setElement(el);
//						handler.endElement();
//					} catch (Exception e1) {
//						e1.printStackTrace();
//					}
//				}
//			};
//			//ds.customRead();	
//			
//		} catch (Exception e1) {
//			e1.printStackTrace();
//			
//		}
//	}
//	
//	public static void set(final IloOplFactory fac, final IloOplModel opl_model, final String name, final int[][] value){
//		try{
//			IloOplFactory f = fac;// IloOplFactory.getOplFactoryFrom(opl_model);
//			IloCustomOplDataSource ds = new IloCustomOplDataSource(f) {
//				
//				@Override
//				public void customRead() {
//					try{
//						IloOplDataHandler handler = getDataHandler();
//						handler.startElement(name);
//						
//						//old code start
//						IloOplElement el= opl_model.getElement(name);
//						int dim1=el.asIntMap().getSize();
//						int dim2=el.asIntMap().getSub(1).getSize();
//										
//						for(int i = 1; i<=dim1; i++){
//							for(int j= 1; j<=dim2; j++){
//								int v=0;
//								if(i<=value.length && j<=value[0].length){		//write zero if model map is larger than value vector
//									v=value[i-1][j-1];
//								}
//								el.asIntMap().getSub(i).set(j, v);
//							}
//						}					
//						//old code end
//						
//						handler.setElement(el);
//						handler.endElement();
//					} catch (Exception e1) {
//						e1.printStackTrace();
//					}
//				}
//			};
//			//ds.customRead();	
//			
//			
//		} catch (Exception e1) {
//			e1.printStackTrace();
//			
//		}
//	}
//	
//	public static void setArray3(final IloOplFactory fac, final IloOplModel opl_model, final String name, final int[][][] value){
//		try{
//			
//			IloOplFactory f = fac;// IloOplFactory.getOplFactoryFrom(opl_model);
//			IloCustomOplDataSource ds = new IloCustomOplDataSource(f) {
//				
//				@Override
//				public void customRead() {
//					try{
//						IloOplDataHandler handler = getDataHandler();
//						handler.startElement(name);
//			
//						//old code
//						IloOplElement el= opl_model.getElement(name);
//						int dim1=el.asIntMap().getSize();
//						int dim2=el.asIntMap().getSub(1).getSize();
//						int dim3=el.asIntMap().getSub(1).getSub(1).getSize();
//						
//						for(int i = 1; i<=dim1; i++){
//							for(int j= 1; j<=dim2; j++){
//								for(int k=1; k<=dim3;k++){
//									int v=0;
//									if(i<=value.length && j<=value[0].length && k<=value[0][0].length){		//write zero if model map is larger than value vector
//										v=value[i][j][k];
//									}
//									el.asIntMap().getSub(i).getSub(j).set(k, v);
//								}
//							}
//						}
//						
//						//old code end
//						
//						handler.setElement(el);
//						handler.endElement();
//					} catch (Exception e1) {
//						e1.printStackTrace();
//					}
//				}
//			};
//			//ds.customRead();	
//			
//		} catch (Exception e1) {
//			e1.printStackTrace();
//			
//		}
//	}
}

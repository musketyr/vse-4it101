

import org.duckapter.annotation.Alias;

public interface Shape {

	@Alias("getŠířka") int getWidth();
	@Alias("getVýška") int getHeight();
	int getX();
	int getY();
	
	public static class Helper {
		
	    public static boolean equals(Shape[] s1, Shape[] s2){
	        if (s1 == null) {
                return s2 == null;
            }
	        if (s2 == null) {
                return false;
            }
	        if (s1.length != s2.length) {
                return false;
            }
	        for (int i = 0; i < s1.length; i++) {
                if(!equals(s1[i], s2[i])){
                    return false;
                }
            }
	        
	        return true;
	    }
	    
		public static boolean equals(Shape s1, Shape s2){
			if (s1 == null) {
				return s2 == null;
			}
			return 
				s1.getWidth() 	== s2.getWidth() 	&&
				s1.getHeight() 	== s2.getHeight()	&&
				s1.getX() 		== s2.getX()		&&
				s1.getY() 		== s2.getY();
		}
		
		public static Shape copy(Shape shape){
			if (shape == null) {
				return null;
			}
			return new ShapeBean(shape);
		}
		
		private static class ShapeBean implements Shape{
			private final int width;
			private final int height;
			private final int x;
			private final int y;
			
			public ShapeBean(Shape shape){
				this(shape.getWidth(), shape.getHeight(),shape.getX(), shape.getY());
			}
			
			public ShapeBean(int width, int height, int x, int y) {
				this.width = width;
				this.height = height;
				this.x = x;
				this.y = y;
			}

			public int getWidth() {
				return width;
			}

			public int getHeight() {
				return height;
			}

			public int getX() {
				return x;
			}

			public int getY() {
				return y;
			}

            @Override
            public String toString() {
                return "ShapeBean [width=" + width + ", height=" + height
                        + ", x=" + x + ", y=" + y + "]";
            }
			
			
		}
		
	}
	
}

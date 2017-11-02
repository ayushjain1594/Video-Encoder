public class douglasPeucker {

    douglasPeucker(int[] points){
        int len=points.length;
        float epsilon=1;
        float maxDist=0;
        int ind=0;

        for (int j=1;j<len-1;j++){
            float dist=perpendicularDistance(j,points[j],0,points[0],len-1,points[len-1]);
            if (dist>maxDist){
                maxDist=dist;
                ind=j;
            }
        }
        if (maxDist>epsilon){
        // Yet to be finished.
        }
    }
    // Calculates perpendicular distance of point x0,y0 from a line joined by x1,y1 and x2,y2
    public float perpendicularDistance(int x0,int y0,int x1,int y1,int x2,int y2){
        double d=Math.abs((y2-y1)*x0-(x2-x1)*y0+x2*y1-y2*x1)/Math.sqrt(Math.pow(y2-y1,2)+Math.pow(x2-x1,2));
        return (float)d;
    }
}

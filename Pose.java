public class Pose {
	public final float x;
	public final float y;
	public final float heading;

	public Pose(float x, float y, float heading) {
		this.x = x;
		this.y = y;
		this.heading = heading;
	}
    
    public boolean equals(Pose p){
        if((x != p.x)||(y != p.y)||(heading != p.heading))
            return false;
        return true;
    }   
}
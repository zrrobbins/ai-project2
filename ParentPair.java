import java.util.List; 

public class ParentPair{
	public final List<Operation> parentOne;
	public final List<Operation> parentTwo;

	public ParentPair(List<Operation> organismOne, List<Operation> organismTwo){
		this.parentOne = organismOne;
		this.parentTwo = organismTwo;
	}
}
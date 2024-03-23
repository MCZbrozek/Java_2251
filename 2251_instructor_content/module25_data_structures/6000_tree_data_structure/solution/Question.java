public class Question // A binary tree
{
	private String question;
	private Question yes_question = null;
	private Question no_question = null;
	
	//Constructor
	public Question(String question)
	{
		this.question = question;
	}
	
	public boolean isTerminatingQuestion()
	{
		return question.startsWith("Is it a");
	}

	public void ask()
	{
		System.out.println(">>>I'm asking: "+question);
	}
	
	public Question setNextQuestion(String q, String yes_or_no)
	{
		if(yes_or_no.equals("yes"))
		{
			yes_question = new Question(q);
			return yes_question;
		}
		else
		{
			no_question = new Question(q);
			return no_question;
		}
	}
	
	public Question getNext(String user_answer)
	{
		if(user_answer.equals("yes"))
		{
			return yes_question;
		}
		else
		{
			return no_question;
		}
	}
}

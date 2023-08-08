import java.io.*;
import java.util.Date;
import org.eclipse.jgit.api.*;

 
 
class git_push()
{
	private void git_add()
	{
		Repository localRepo = new FileRepository("~/Documents/takesue090/java/.git");
		Git git = new Git(localeRepo);
		if(git !=null)
		{
			AddCommand add_command = git.add();
			add_command.addFilepattern(".");
			try
			{
				add_command.call();
				System.out.prinltn("success add .");
				CommitCommand commit_command = git.commit();
				Date times = new Date();
				String time = times.toString();
				commit_command.setCommitter("v02qabs", "takesue090@gmail.com").setMessage(time);
				commit_command.call();
				System.out.println("commit command success");
				-
			}
			catch(Exception error)
			{
				System.out.println(error.toString());
			}
		}
	}
	public static void main(String[] args)
	{
		System.out.println("Hello jgit." );
		new git_push();
	}
	public git_push()
	{
		git_add();
	}
}


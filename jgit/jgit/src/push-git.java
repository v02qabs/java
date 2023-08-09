import java.io.*;
import java.util.Date;
import org.eclipse.jgit.api.*;
import org.eclipse.jgit.transport.ssh.jsch.*;
import org.eclipse.jgit.transport.*;
import org.eclipse.jgit.transport.ssh.jsch.OpenSshConfig;
import com.jcraft.jsch;

class git_push
{
	private void SshClone() throws Exception
	{
		SshSessionFactory factory = new JschConfigSessionFactory()
		{
			@Override
			protected void configure(Host host , Session session)
			{
				session.setPassword("zclible085");
			}
		};
		CloneCommand cloneCommand = Git.cloneRepository();
		CloneCommand.setURI("https://github.com/v02qabs/win32api.git");
		cloneCommand.setDirectory(new File("/home/takesue090/git/j1/."));
		cloneCommand.setCredentalsProvider("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", "");
		clneCommand.setTransportConfigCallback(new TransportConfigCallback()
		{
			@Override
			public void configure(Transport transport)
			{
				SshTransport ssh_transport = (SshTransport) transport;
				ssh_transport.setSshSessionFactory(factory);
			}
		});
		cloneCommand.call();
	}
	
	public static void main(String[] args)
	{
		System.out.println("Hello jgit." );
		new git_push();
	}
	public git_push()
	{
		try
		{
			SshClone();
		}
		catch(Exception error)
		{
			System.out.println("error");
		}
		
	}
}


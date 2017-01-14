package com.gerken.xaa.sme;

public class MentorHerder {

	private static MentorHerder	instance;
	
	private MentorHerder() {
	}

	public static MentorHerder getInstance() {
		if (instance == null) {
			instance = new MentorHerder();
		}
		return instance;
	}

	public IXaaMentor[] getMentors() {
		IXaaMentor[] result = new IXaaMentor[] {
				new BasicMentor(),
				new JavaMentor(),
				new MavenMentor(),
				new Ios4Mentor(),
				new IntelliJIdeaMentor(),
				new GitMentor()
		};
		return result;
	}
}

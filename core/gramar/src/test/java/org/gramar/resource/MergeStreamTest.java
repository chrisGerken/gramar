package org.gramar.resource;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

public class MergeStreamTest {

	@Test
	public void testSaveRegionChanges() {
		
		MergeStream stream;
		
		try {
			stream = streamWithCode("abcd", "// before", "original", "// after", "wxyz");
			String original = stream.toString();
			
			stream = streamWithCode("abcd", "// beforeX", "newer", "// afterX", "wxyz");

			String correct = stream.toString();
			String result = stream.saveRegionChanges(original).toString();
			
			if (!correct.equals(result)) {
				fail("Improperly saved user region code");
			}
		} catch (IOException e) {
			fail("Error: "+e);
		}
		
	}

	private MergeStream streamWithCode(String before, String eyeBefore, String code, String eyeAfter, String after) throws IOException {
		MergeStream stream = new MergeStream();
		stream.append(before);
		UserRegion ur = new UserRegion(stream);
		ur.markUserRegionStart();
		stream.append(eyeBefore);
		ur.markInitialCodeStart();
		stream.append(code);
		ur.markInitialCodeEnd();
		stream.append(eyeAfter);
		ur.markUserRegionEnd();
		stream.append(after);
		stream.addUserRegion(ur);
		return stream;
	}
}

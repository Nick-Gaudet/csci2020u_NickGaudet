<h1>Multithreaded File Sharing System</h1>
<h2>Information</h2>
<img src=".\system.jpeg">
<p></p>
 
 <h2>Improvements</h2>
<p>Added a little delete button to delete any files in the clients directory.<p>
<p> A note on setting <code>hostName</code>: I tried to get user input to work for setting the <code>hostName</code> variable, however when setting the socket it would always return a connection refused if I passed user inputed <code>hostName</code> to the socket, however when <code>hostName</code> is hard coded to a string variable it works, not sure why, and time wise this was not worth the fix compared to all other issues that came up.
 
 <h2>How-To-Run</h2>
 <p>First make sure javafx is installed on your machine. If not follow this link first: <a href="https://openjfx.io/openjfx-docs/">JavaFX</a></p>
 <ul>
 <li>Download or Clone</li>
 <li>In the <code>Server.java</code> file change the hostName field variable to the current machine Ipv4 address (0.0.0.0), do the same to the <code>Controller.java</code> in the client folder.</li>
 <li>Once you've done this, run <code>Server.java</code> first, the server is now running, any subsequent client opened should have <code>hostName</code> set to the server machine Ipv4.</li>
  </ul>

  
<h2>References</h2>
<p>
<a href="https://docs.oracle.com/en/java/javase/16/docs/api/index.html">https://docs.oracle.com/en/java/javase/16/docs/api/index.html </a> <br />

</p>
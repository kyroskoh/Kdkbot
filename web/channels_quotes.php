<?PHP
	$filPath = "../cfg/#" . $_GET['c'] . "/quotes.cfg";
	if(file_exists($filPath)) {
		$fil = file_get_contents($filPath);
		$quotes = explode("\r\n", $fil);
		asort($quotes);
		
		echo "<br /><p>Quotes list for " . ucwords(strtolower($_GET['c'])) . "</p>";
		echo "<table class=\"minTable\"><tr><th>ID</th><th>Quote Text</th></tr>";
		foreach ($quotes as $quote) {
			if($quote !== "") {
				$quote_segments = explode(": ", $quote, 2);
				echo "<tr><td>" . $quote_segments[0] . "</td><td>" . $quote_segments[1] . "</td></tr>";
			}
		}
		if(count($quotes) <= 1) { 
			echo "<tr><td colspan=\"2\">There are no quotes for this channel :(</td></tr>";
		}
		echo"</table>";
	} else {
		require("error.php");
	}
?>
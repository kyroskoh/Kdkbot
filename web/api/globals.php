<?PHP
	/* Determines if a given string (haystack) contains another string (needle).
	 * Returns true if found, false otherwise
	 */
	function startsWith($haystack, $needle) {
		if(substr($haystack, 0, strlen($needle)) === $needle) {
			return true;
		} else {
			return false;
		}
	}
	
	/* Gets the base configuration setting location found in the settings.ini file
	 * Returns a string of what was found for a particular value. May return null.
	 */
	function getBaseConfigSetting() {
		$ini_contents = parse_ini_file("../cfg/settings.ini", true);
		return $ini_contents["Bot"]["cfgLocation"];
	}
	
	function getChannelLocation($channel) {
		if(!startsWith($channel, "#")) {
			$channel = "#" . $channel;
		}
		$path = getBaseConfigSetting() . "\\$channel";
		if(file_exists($path)) {
			return $path;
		} else {
			return false;
		}
	}
	
	function getChannelObject($channel) {
		$path = getChannelLocation($channel);
		if($path === false) {
			return false;
		} else {
			return new Channel($path);
		}
	}
	
	class Channel {
		public $baseLocation;
		
		public function __construct($baseLocation) {
			$this->baseLocation = $baseLocation;
		}
		
		public function pathAMA() {
			return $this->baseLocation . "\\ama.cfg";
		}
		
		public function pathChannel() {
			return $this->baseLocation . "\\channel.cfg";
		}
		
		public function pathCommands() {
			return $this->baseLocation . "\\cmds.cfg";
		}
		
		public function pathCounters() {
			return $this->baseLocation . "\\counters.cfg";
		}
		
		public function pathEconomy() {
			return $this->baseLocation . "\\economy.cfg";
		}
		
		public function pathEconomyUsers() {
			return $this->baseLocation . "\\economy_users.cfg";
		}
		
		public function pathFilters() {
			return $this->baseLocation . "\\filters.cfg";
		}
		
		public function pathPerms() {
			return $this->baseLocation . "\\perms.cfg";
		}
		
		public function pathQuotes() {
			return $this->baseLocation . "\\quotes.cfg";
		}
		
		public function pathStats() {
			return $this->baseLocation . "\\stats.cfg";
		}
		
		public function pathTimers() {
			return $this->baseLocation . "\\timers.cfg";
		}
		
		public function pathTokens() {
			return $this->baseLocation . "\\tokens.cfg";
		}
	}
?>
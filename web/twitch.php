<?PHP
// Lets get the information needed from ../cfg/settings.cfg
$cfgFile = file_get_contents("../cfg/settings.cfg");
$cfgFileLines = explode("\r\n", $cfgFile);

foreach($cfgFileLines as $cfgFileLine) {
	if(substr($cfgFileLine, 0, strlen("clientId=")) === "clientId=") { 
		$clientID = explode("=", $cfgFileLine,2 )[1];
	} elseif(substr($cfgFileLine, 0, strlen("twitchSecret=")) === "twitchSecret=") {
		$clientSecret = explode("=", $cfgFileLine, 2)[1];
	} elseif(substr($cfgFileLine, 0, strlen("redirectURI=")) === "redirectURI=") {
		$redirectionURI = explode("=", $cfgFileLine, 2)[1];
	}
}

// We should have arrived here with a token in $_GET['code'] - something went wrong if we got here otherwise
if(!isset($_GET['code'])) { die(); }

// Compile the parameters we need
$params = ['client_id' => $clientID, 'client_secret' => $clientSecret, 'grant_type' => 'authorization_code', 'redirect_uri' => $redirectionURI, 'code' => $_GET['code']];

// echo "<pre>PARAMS: " . print_r($params, true) . "</pre>";

// Lets finally get that oauth token!
$url = 'https://api.twitch.tv/kraken/oauth2/token';

$ch = curl_init($url);
curl_setopt($ch,CURLOPT_POSTFIELDS,$params);
curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);

$oAuth = json_decode(curl_exec($ch), true);
curl_close($ch);

// echo "<pre>OAUTH: " . print_r($oAuth, true) . "</pre>";

// We need to store these tokens in the event we need them for later, but we need to get the channel they belong to first
if(isset($oAuth['access_token'])) {
	$userData = json_decode(file_get_contents('https://api.twitch.tv/kraken?oauth_token=' . $oAuth['access_token']), true);
} else {
	$userData = "";
}

// echo "<pre>USER_JSON: " . print_r($userData, true) . "</pre>";

if(isset($userData["token"]["user_name"])) { 
	$username = $userData["token"]["user_name"];
} else {
	$username = "";
}

// echo "<pre>USERNAME: " . print_r($username, true) . "</pre>";

// With $username containing the username, we can finally put this information for their channel:
$userCfgFile = "../cfg/#".$username."/tokens.cfg";
if($username !== "") {
	$data = "accessToken=".$oAuth['access_token']."\r\n";
	$data .= "refreshToken=".$oAuth['refresh_token']."\r\n";
} else {
	$data = "";
}

if($data == "" || file_put_contents($userCfgFile, $data) === false) {
	echo "<div class=\"boxError\">We could not add the information required to kdkbot. Please try again. If the issue persists, please contact the webmaster.</div>";
} else {
	echo "<div class=\"boxSuccess\">You have successfully permitted kdkbot access to your channel information.</div>";
}

?>

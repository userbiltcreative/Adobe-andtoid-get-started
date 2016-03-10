# Android: Getting Started

This repo accompanies [the Creative SDK Getting Started guide for Android](https://creativesdk.adobe.com/docs/android/#/articles/gettingstarted/index.html).


## How to use

Just follow the steps below.

### In your browser

1. [Register a new app for the Creative SDK](https://creativesdk.adobe.com/myapps.html)
1. Note your Client ID and Secret. You will need them soon.

### In your local development environment

1. `git clone` the repo
1. Add a new Java class called `Keys` with this code:  

	```
	public class Keys {

	    public static final String CSDK_CLIENT_ID = "<YOUR_ID_HERE>";
	    public static final String CSDK_CLIENT_SECRET = "<YOU_SECRET_HERE>";

	}
	```

    1. Add your Client ID and Secret to the `Keys` class
    1. This class is gitignored so you can avoid exposing your keys in GitHub
1. Sync your Gradle files and run the app
# Creative SDK v0.9.2006-5
November 27, 2016

## Improvements

- Android O Support
- Bug fixes for performance and stability

## Deprecations

The following components are deprecated and no longer supported:

- Image Editor UI
- Color UI
- Market Browser
- Labs

[See the Creative SDK blog for more details](https://blog.creativesdk.com/2017/09/important-update-to-the-creative-sdk-end-of-support-for-the-image-editor-ui-color-ui-market-browser-and-labs-components/).



# Creative SDK v0.9.1186
November 2, 2016

**IMPORTANT:** If you are updating to v0.9.1186 and above, you will need to register a new API Key, Secret and redirectURL from the new [Adobe I/O Console](https://console.adobe.io/integrations).


## Android N (API 24) Support

- The Creative SDK now supports Android N (API 24). This is now the maximum API level supported.
API Keys and Secrets
- Instead of the previous Client ID/Secret authentication, Client Auth for the Creative SDK now requires a new API Key, Client Secret, and unique Redirect URL. These credentials can be generated on the new [Adobe I/O Console](https://console.adobe.io/integrations).

## Redirect URI

- When you register a new Integration or generate new Client Auth credentials for an existing Integration (as mentioned above), you will be given a Redirect URI, which is used as part of the User Auth workflow.
- See our User Auth UI guide for implementation details.

## Image Editor (v4.8.3) updates

- New optional method `quickLaunchTool()` lets you auto-launch a designated tool when the Image Editor is launched.
- New optional method `forceCrop()` lets you require users to crop an image to a specified ratio before making any other edits.
- New `IAdobeAuthRedirectCredentials` interface requirement for your `Application` subclass.

See our Image Editor UI guide for implementation details.

# Creative SDK v0.9.1062
August 2, 2016

## Asset Browser UI

- Adds support for Read Only Folders and Read Only Libraries
- Adds Pattern Support

## Adobe Labs

- Initial release! Features:
    - Magic Path
    - Magic Curve
    - Perspective View

## Behance UI

- Logout issue fix seen using Behance App
- Behance Project Viewer updated
- CancelPublish, AdobeBehanceSDKProjectPublishOptions APIs added

## Color UI

- Updates to Color SDK component to support Android Tablet layouts

## Image Editor UI

- Adobe ID sign for users to access and sync more free Effects, Stickers, Frames and Overlays.

## Send To Desktop API

- Changes to sendData() method:
    - Now accepts inputStream instead of byte[]
- New methods:
    - sendToDesktop(AdobeAssetFile asset, AdobeCreativeCloudApplication application, IAdobeSendToDesktopCallBack sendCallback)
    - sendToDesktop(Uri uri , AdobeCreativeCloudApplication application, IAdobeSendToDesktopCallBack sendCallback)
    - sendToDesktop(Bitmap bitmap,  String name, AdobeCreativeCloudApplication application, IAdobeSendToDesktopCallBack sendCallback)
    - sendToDesktop(InputStream data, String name, String mimeType, AdobeCreativeCloudApplication application,  IAdobeSendToDesktopCallBack sendCallback)
- The following APIs have been deprecated:
    - sendImage(Bitmap bitmap, AdobeCreativeCloudApplication application, String name, IAdobeSendToDesktopCallBack sendImageCallback)
    - sendAsset(AdobeAssetFile asset, AdobeCreativeCloudApplication application, String name, IAdobeSendToDesktopCallBack sendAssetCallback)
    - sendLocalFile(String filePath, String fileType, AdobeCreativeCloudApplication application, String name, IAdobeSendToDesktopCallBack sendLocalFileCallback)


## Typekit UI

- Initial release! Features:
    - Ability to search fonts
    - Ability to sync fonts and download them

***

# Creative SDK v0.9.7
March 3, 2016

## AdobeCreativeSDKFoundation

- Android API 23 Support
    - Android M runtime permissions required by the SDK components are handled by the SDK
- Asset Browser Enhancements
    - Folders show a rendition of the last updated file in the folder icon
- Send to Desktop APIs have been updated with the following new APIs:
    - sendLocalFile()
    - Refer to the API documentation for more details
- Bug fixes in all components

## Image Editor
- The Image Editor class has been renamed to AdobeImageIntent
- Updated UI with Material Deisgn
- New "Adjust" tool
- You can now offer your Image Editor users access to Additional Content packs of effects, frames, stickers, and overlays. [See this article](https://creativesdk.zendesk.com/hc/admin/articles/207914166-Getting-Additional-Content-for-the-Image-Editor) on our Knowledge Base to learn more.</a>

***

# Creative SDK v0.7.329
October 29, 2015

## AdobeCreativeSDKFoundation

- The Foundation SDK has been broken up into four smaller SDKs to help keep the size of your app down. Below is a list of the new Framework breakup:
    - CreativeSDKFoundationAppLibrary
    - CreativeSDKFoundationAssetCore
    - CreativeSDKFoundationAssetUX
    - CreativeSDKFoundationAuth
- The old monolithic CreativeSDKFoundationAssets has been split into CreativeSDKFoundationAssetCore and CreativeSDKFoundationAssetUX.
- The new CreativeSDKFoundationAssetCore contains the Creative SDK's headless Asset APIs.
- The new CreativeSDKFoundationAssetUX contains the Creative SDK's Asset Browser UI components.
- The new CreativeSDKFoundationAppLibrary showcases apps that use the Creative SDK.

## CreativeSDKColor
- A new UI component for Android that offers:
    - A Color Picker
    - Color Themes
    - Access to Color CC Libraries

***


# Creative SDK v0.5.3
July 27, 2015

## AdobeCreativeSDKFoundation
- Miscellaneous bug fixes

***

# Creative SDK v0.4.264
June 4, 2015

## AdobeCreativeSDKFoundation

- The Asset Browser UI and Authentication Component is now localized in 18 languages:
    - English, French, German, Japanese, Italian, Spanish, Russian, Korean, Portugese, Polish, Simplified Chinese, Traditional Chinese, Swedish, Turkish, Danish, Dutch, Finnish, Norwegian.
- New API for setting userAgent has been added to AdobeCSDKFoundation: AdobeCSDKFoundation.initializeAppInfo(Appname,VersionString)
- New APIs for working directly with Creative Cloud Libraries.
- New APIs to create and manage Creative Cloud Libraries.
- New APIs to add colors, text styles and images to Creative Cloud Libraries.
- Improvements in the SendToDesktop API:
    - New sendAsset method supports sending a file already stored in Creative Cloud to the desktop.
    - DEPRECATED. sendItem method has been replaced by the above methods and will be removed from the SDK in a future release.
- New APIs to access Mobile Creations(PSMix,Draw,Line,Sketch,Comp).

##Asset Browser##

- Adds support for Mobile Creations (content from PSMix, Draw, Line, Sketch, and Comp).
- Adds support for Libraries.
- Adds support for viewing multi-page documents.

## AdobeCreativeSDKImage

- Miscellaneous Bug fixes.

## AdobeCreativeSDKBehance

- Behance Component is now localized in 18 languages:
    - English, French, German, Japanese, Italian, Spanish, Russian, Korean, Portugese, Polish, Simplified Chinese, Traditional Chinese, Swedish, Turkish, Danish, Dutch, Finnish, Norwegian.
- Adds Behance Profile APIs for editing a user's public profile.
- Supports publishing WIP content and ability to cancel when upload is in progress.

***

# Creative SDK v0.3.94
February 16, 2015

## AdobeCreativeSDKFoundation

- New APIs for creating PSD and AI documents.
Known Issues:
- The Creative SDK is not yet supported for tablet form factors.
- Mismatch of localization across components.

## Asset Browser

- Development Status: Stable
- Visual component that provides access to files stored in the Creative Cloud and Lightroom photos.
- Updated experience and design for Android L.
- New localizations:
    - French
    - German
    - Japanese
- Streaming previews for videos stored in the Creative Cloud.

##Auth

- Development Status: Stable
- Visual component that provides support for end-user login to the Creative Cloud.
- Renamed AdobeCSDKTheme.Activity to AdobeCSDKThemeActivity.
    - This theme is used in the CreativeSDKSampleApp. In the event of an update, change the theme name in AndroidManifest.xml:
    &lt;activity&gt;…android:theme="@style/AdobeCSDKThemeActivity"&gt;&lt;/activity&gt;


## AdobeCreativeSDKImage

- Development Status: Stable
- Visual image editor component (formerly the Aviary SDK).
- No improvements with this release.

## AdobeCreativeSDKBehance

- Development Status: Stable
- Enable users to publish work-in-progress (WIP) and projects to Behance
    - Work-in-progress: A single image, commonly used to represent an unfinished work.
    - Project: A composed piece, including one or more images/video embeds, and a cover image.
- Updated experience and design for Android L.
- Prior to posting a WIP or project, Creative Cloud Enterprise users are alerted to the fact that they are publishing under their company account as opposed to their personal account. If a user publishes content to Behance under their company account they may lose access to that work if they leave their company.
- For deeper integrations with Behance, framework includes a wrapper for the Behance API which can be found at [http://www.behance.net/dev](http://www.behance.net/dev).


***


# Creative SDK v0.2.10
December 15, 2014

## AdobeCreativeSDKFoundation

- Includes two components for accessing assets stored in the Creative Cloud and authorization.
- New support for portrait and landscape mode.
- Supports Android API 21. Minimum supported version is API 14.
- Allows for Creative Cloud sign-up as well as sign-in.
Known Issues:
- The Creative SDK is not yet supported for tablet form factors.

## Asset Browser

- Development Status: Beta
- Visual component that provides access to files stored in the Creative Cloud.
- Support for single selection mode and multi-selection mode.
- Configurable filtering based on datasource and mimetypes.
- NEW APIs for access Creative Cloud files and Lightroom photos.
- NEW APIs for Send to Photoshop and Send to Illustrator.

## Auth

- Development Status: Stable
- Visual component that provides support for end-user login to the Creative Cloud.
- Support for Creative Cloud sign-up in addition to sign-in.
- Support for Type-2 and Type-3 Adobe IDs.

## AdobeCreativeSDKImage

- Development Status: Stable
- Image editor component (formerly the Aviary SDK).
- NEW Vignette tool.
- NEW Overlay tool.
- NEW Lighting and Color tools.
- Sticker flip and sticker opacity.
- Simplified high resolution API.

## AdobeCreativeSDKBehance

- Development Status: Beta
- Enable users to publish work-in-progress (WIP) and projects to Behance
    - Work-in-progress: A single image, commonly used to represent an unfinished work.
    - Project: A composed piece, including one or more images/video embeds, and a cover image.
- Project images can be added from user photo library as well as from Creative Cloud.
- Ability to share work to Facebook and/or Twitter upon publishing to Behance.
- Publish process can now be switched to a background process.
- For deeper integrations with Behance, framework includes a wrapper for the Behance API which can be found at [http://www.behance.net/dev](http://www.behance.net/dev).

***


# Creative SDK v0.1.100
October 5, 2014

## Auth
- Development Status: Stable
- Visual component that provides support for end-user login to the Creative Cloud.
- Provides authentication token which is required by Creative Cloud API.
- Allows for Creative Cloud sign-up as well as sign-in.

## Asset Browser
- Development Status: Stable
- Visual component that provides access to files stored in the Creative Cloud.
- Support for single selection mode and multi-selection mode.
- Basic sorting and search/filtering.

## AdobeCreativeSDKBehance
- Development Status: Stable
- Enable users to publish work-in-progress (WIP) and projects to Behance
    - Work-in-progress: A single image, commonly used to represent an unfinished work.
    - Project: A composed piece, including one or more images/video embeds, and a cover image.
- Project images can be added from user photo library as well as from Creative Cloud.
- Ability to share work to Facebook and/or Twitter upon publishing to Behance.

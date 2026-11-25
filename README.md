# Detailed description

Article on Medium: coming soon

# Short description

This Android app demonstrates edge-to-edge support for Views and Compose on Android 6+:
1. ```enableEdgeToEdge``` with transparent system bars that always match the content.
2. ```applySystemBarsPadding``` for Views.
3. ```systemBarsScreenPadding``` and other modifiers for Compose.
4. Applying system insets only for full screens: bottom sheets, dialogs and embedded content handle them on their own.
5. Keyboard handling: the content stays above the open keyboard.
6. Support of Android 10 and lower, where insets may not reach views at all.
7. Support of Android 7.1 and lower, where the navigation bar cannot have dark icons.

The app is available only in portrait orientation, so horizontal insets are not handled.

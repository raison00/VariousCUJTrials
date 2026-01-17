# VariousCUJTrials
Testing issues and how legacy API and modern API can tangle and trap within the Focus and Input Management System.    These are all very basic, generic apps with various methods to achieve an adaptive approach to focus, traversal, and input across most Android devices (excluding glasses and xr).  This will result in intentionally not functioning UI to observe the conflicts of mixing APIs and legacy ways to achieve the same goal.

Screen recordings from the AndroidStudio IDE of these apps can be found here:  [Compose CUJ tests with Focus Management and M3. ](https://www.youtube.com/channel/UC2m3-lR1mBnyrhTZl3f8gbg)
Reach out if you'd like context on a specific app and its goal.  The apps in this repositiory are not production-ready code.  

## Project Overview
This repository documents the architectural patterns and logic used to resolve complex Input and Focus behaviors within the Android ecosystem. The primary focus was on surfacing focus and traversal issues and solutions for a best in class adaptive experience compliant with WCAG AA.

## Core Focus Areas
* Unified Traversal Logic: Methods for handling focus movement across disparate UI frameworks (Native vs. WebView) to ensure the user never "loses" their place.

*  Keyboard & TextField Interaction: Resolving edge cases in keyboard focus cycles and text input focus states.

*  Blocker Resolution: Tactical fixes for underlying system bugs that prevent Critical User Journeys (CUJs) from functioning as intended.

*  Accessibility (A11y): Implementing focus-order logic that meets WCAG AA standards for screen readers and hardware keyboard users.

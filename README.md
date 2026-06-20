# Android Focus & Traversal Patterns for Adaptive UI

> Documents the focus management and keyboard/D-pad traversal patterns I developed for Jetpack Compose at Google — resolving focus traps and broken traversal order across Mobile, Foldable, TV, Auto, and Desktop form factors.

**Status:** Active research / internal patterns adapted for public reference
**My role:** UI Design Lead — Adaptive UI & Design Systems, Google (via Apex)
**Compliance target:** WCAG 2.1 §2.1.2 (No Keyboard Trap), §2.4.3 (Focus Order)

---

## The problem

Compose's default focus traversal order is inferred from composition order, not visual or logical order. This works fine on a single phone screen. It breaks down hard across form factors: a Foldable that changes layout on fold/unfold, a TV interface navigated entirely by D-pad, or a Desktop window where mouse and keyboard focus need to stay in sync. Developers were hitting focus traps — UI elements that could be reached but never escaped via keyboard or D-pad — with no consistent pattern library to draw from.

## What this repo documents

This repo captures the architectural patterns and design logic used to identify, reproduce, and resolve focus and traversal issues across the Android ecosystem, with a specific focus on adaptive layouts that change shape across devices.

- `/traps` — documented focus trap scenarios, how they were identified, and the fix pattern
- `/traversal-order` — explicit vs. inferred traversal order patterns in Compose
- `/form-factor-matrix` — how traversal behavior needs to differ across Mobile, Foldable, TV, Auto, Tablet, Desktop
- `/before-after` — annotated before/after recordings of specific focus trap fixes

## My role vs. the team

**What I owned:** Identifying and reproducing focus traversal failures, authoring the design-side fix patterns and interaction specs, partnering directly with Compose engineering to validate proposed solutions against WCAG 2.1.2 and 2.4.3.

**What the team owned:** Implementation in the Compose focus API, the underlying `FocusManager` and `FocusRequester` architecture, and the official fix timeline tracked in the Compose Issue Tracker.

## Key design decisions

**Explicit traversal groups over relying on inferred order.** Inferred composition-order traversal works for simple layouts but breaks immediately once a layout adapts across form factors. The pattern documented here uses explicit `focusGroup()` and `focusOrder()` boundaries at every adaptive breakpoint, rather than trusting default inference to hold across breakpoints.

**Designing the fix at the pattern level, not the screen level.** Early work treated each focus trap as a one-off bug. The shift documented in `/form-factor-matrix` was to define a reusable traversal contract per form factor — so new screens inherit correct behavior by default instead of needing a bespoke fix each time.

**Validating against WCAG explicitly, not just "it feels right."** Every pattern in `/traps` is mapped to the specific WCAG success criterion it satisfies. This was a deliberate choice to keep the work auditable and defensible in cross-functional design reviews, not just intuitively "better."

## What I'd do differently

Some of the earliest pattern documentation in this repo predates the form-factor matrix approach — a few of the `/traps` entries describe one-off fixes rather than the reusable pattern. A cleaner version of this repo would retroactively map every early fix back into the matrix framework so the whole repo speaks one consistent design language.

## Related

- Material Design 3 for Flutter: [m3.material.io/develop/flutter](https://m3.material.io/develop/flutter)
- Android Compose focus documentation: [developer.android.com/develop/ui/compose/touch-input/focus](https://developer.android.com/develop/ui/compose/touch-input/focus)
- Design walkthrough video series: [YouTube — Focus Management Strategy](https://www.youtube.com/channel/UC2m3-lR1mBnyrhTZl3f8gbg)



### VariousCUJTrials
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

 ## Screen recordings from these apps are available on YT

[![Watch the video](https://img.youtube.com/vi/fQREPFzanug/maxresdefault.jpg)](https://www.youtube.com/shorts/fQREPFzanug)

This respository is for creating a Focus Input, TextField, and Traversal Demonstration on Android 16, Jetpack Compose 2025.12.01, Material 3 1.4.0.

Jetpack Compose was designed around an implicit focus model, creating challenges when transitioning to explicit focus models required by devices like Phones, Chromebooks, Android TV and Android Auto.  This forces developers to manually manage the Focus Tree, leading to accessibility failures.


The TextField can consume the Tab key press, inserting a Tab character into the text string rather than shifting focus to the next element.

The user is able to type into a TextField but is not able to see the typed characters within the TextField because the input value is not correctly captured, retained and re-rendered, causing the text to disappear. 
This issue is particularly problematic in dynamic forms and navigation scenarios where TextFields can be composed and removed from the composition tree.


Focus trapping in modal dialogs is a critical feature to ensure accessibility, confining keyboard and assistive technology focus within the dialog.


When using Dialog or Popup composables, focus is automatically moved to the first focusable element. However, focus can get "stuck" on the last element when using the Tab key, requiring users to press Shift+Tab to move backward if the device allows for it.

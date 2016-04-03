# Release 0.6.5 #
(2007-06-03)


### Bug fixing ###

  * Demo fixes


# Release 0.6.5 #
(2007-06-02)

This Release is essentially  compliant with GWT 1.4.

### Improvements ###

  * Applet Management improvements
  * No more white spaces at the bottom of the windows  when slim themed borders are used
  * No more 3rd lib dependencies
  * Performances increased.

# Release 0.6 #
(2007-04-19)

### New Features ###

  * Hide effect when closing frames [issue 11](http://code.google.com/p/gwtwindowmanager/issues/detail?id=11&can=1&q=)
  * 4 new great themes added to the release
  * Setting a theme on GDesktopPane changes all the themes of its GInternalFrame's [issue 20](http://code.google.com/p/gwtwindowmanager/issues/detail?id=20&can=1&q=)
  * Different themes for selected and unselected frames [issue 26](http://code.google.com/p/gwtwindowmanager/issues/detail?id=26&can=1&q=)
  * GFrameAdapter added for easier listener management [issue 27](http://code.google.com/p/gwtwindowmanager/issues/detail?id=27&can=1&q=)

### Improvements ###

  * Better code architecture by refactoring into GFrame/GInternalFrame/GDialog
  * Direct injection of the default theme and javascript files dependencies
  * Frames with URL content are no longer reloaded when resized or dragged [issue 11](http://code.google.com/p/gwtwindowmanager/issues/detail?id=11&can=1&q=) and [issue 31](http://code.google.com/p/gwtwindowmanager/issues/detail?id=31&can=1&q=)
  * Faster dragging of frames with different drag modes
  * Direct instantiation (besides showXXX() methods) of GDialog possible for more customized dialogs [issue 24](http://code.google.com/p/gwtwindowmanager/issues/detail?id=24&can=1&q=)

### Bug fixing ###

  * ListBoxes are no longer visible through frames [issue 18](http://code.google.com/p/gwtwindowmanager/issues/detail?id=18&can=1&q=)
  * GInternalFrame's visibility bugs have been fixed [issue 19](http://code.google.com/p/gwtwindowmanager/issues/detail?id=19&can=1&q=) and [issue 28](http://code.google.com/p/gwtwindowmanager/issues/detail?id=28&can=1&q=) and [issue 30](http://code.google.com/p/gwtwindowmanager/issues/detail?id=30&can=1&q=)
  * Maximize/Minimize functionality bugs have been fixed [issue 22](http://code.google.com/p/gwtwindowmanager/issues/detail?id=22&can=1&q=) and [issue 32](http://code.google.com/p/gwtwindowmanager/issues/detail?id=32&can=1&q=)
  * Closed frames will set all references to null so that it can be garbage collected [issue 29](http://code.google.com/p/gwtwindowmanager/issues/detail?id=29&can=1&q=)
  * Dragging frames is prevented from being dragged outside of viewable area and loosing access to the topbar [issue 25](http://code.google.com/p/gwtwindowmanager/issues/detail?id=25&can=1&q=)


# Release 0.5.2 #
(2007-02-07)
### Major changes ###
N/A
### Bug fixing ###

  * [issue 17](https://code.google.com/p/gwtwindowmanager/issues/detail?id=17) : the setMaximizable(), setCloseable() fixed.
  * The wineditor in the demo creates the customized frame.


# Release 0.5.1 #
(2007-02-07)

Packaging fixes.

# Release 0.5 #
(2007-02-06)

The 0.5 release provides the foundation for further enhancements.
Developers can create Free Frames that float everywhere around inside
the browser window, or they can create a Desktop that manages the Frames.

### Major changes ###
  * GWT-implementation of the GInternalFrame (no external js-libraries)
  * Free frames and desktop-managed frames
  * Dialog and Input frames
  * added behaviour capabilities
  * event-catching and debug window
  * bug-fixes
  * added samples with source code

Behaviour capabilities:
  * frames can be dragged and resized
  * a frame caption has 3 buttons: minimize/restore, maximize/restore, close

# Release 0.1b #
(2006-12-10)

The 0.1b release provides the first implementation of Gwm based on the PWC lib.
Developers can create Free Frames that float everywhere around inside
the browser window.
The look and feel of the Frames is defined by themes. The 0.1b release comes with a number of themes, and users can create their own themes by creating a css file and some images.
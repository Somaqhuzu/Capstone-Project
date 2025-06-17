JAVAC=/usr/bin/javac
JAVA=/usr/bin/java
JAVAFX_LIB=/home/skhoma/javafx-sdk-24.0.1/lib

.SUFFIXES: .java .class

BINDIR=bin
SRCDIR=src

CLASSES=\
	Bacteria.class\
    Surface.class\
    SurfaceBlock.class\


CLASS_FILES=$(CLASSES:%.class=$(BINDIR)/%.class)

JFLAGS=--module-path $(JAVAFX_LIB) --add-modules javafx.controls

default: $(CLASS_FILES)

$(BINDIR)/%.class: $(SRCDIR)/%.java
	$(JAVAC) $(JFLAGS) -d $(BINDIR) $<

run: default
	$(JAVA) $(JFLAGS) -cp $(BINDIR) Surface

clean:
	rm -r $(BINDIR)/*.class

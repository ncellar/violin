VERSION:=0.1.0
KOTLIN_VER:=1.0.3
TESTNG_VER:=6.9.10
JCOMMA_VER:=1.48
DOKKA_VER:=0.9.8

TESTNG:="http://central.maven.org/maven2/org/testng/testng/$(TESTNG_VER)/testng-$(TESTNG_VER).jar"
JCOMMA:="http://central.maven.org/maven2/com/beust/jcommander/$(JCOMMA_VER)/jcommander-$(JCOMMA_VER).jar"
DOKKA:="https://github.com/Kotlin/dokka/releases/download/$(DOKKA_VER)/dokka-fatjar.jar"
KOTLIN:="https://github.com/JetBrains/kotlin/releases/download/v$(KOTLIN_VER)/kotlin-compiler-$(KOTLIN_VER).zip"

ifeq ($(shell if [ -d lib/kotlinc ]; then echo "yes"; fi),yes)
	KOTLINC:=lib/compiler/kotlinc
else
	KOTLINC:=kotlinc
endif

ifeq ($(OS),Windows_NT)
	SEP:=;
else
	SEP:=:
endif

# defines BINTRAY_USER and BINTRAY_API_KEY
include local.mk

space:=$(eval) $(eval)
# java supports "lib/*", but not kotlin
jars:=$(subst $(space),$(SEP),$(shell find lib -name "*.jar"))
cp:="out/production$(SEP)$(jars)"
testcp:=$(cp)$(SEP)out/test

build:
	mkdir -p out/production
	$(KOTLINC) -cp $(cp) src -d out/production

buildtests:
	mkdir -p out/test
	$(KOTLINC) -cp $(testcp) test -d out/test

test:
	java -cp $(testcp) org.testng.TestNG test/testng.xml -d out/test-output

refresh: build buildtests test

clean:
	rm -rf out

fetchkotlin:
	curl -L $(KOTLIN) > lib/compiler.zip
	mkdir -p compiler
	unzip lib/compiler.zip -d lib
	rm lib/compiler.zip

fetchlibs:
	mkdir -p lib
	curl -L $(DOKKA)	  > lib/dokka.jar
	curl -L $(TESTNG)	  > lib/testng.jar
	curl -L $(JCOMMA)     > lib/jcommander.jar

jar:
	jar cf out/violin-$(VERSION).jar out/production/*

docs:
	mkdir -p out/docs/java
	mkdir -p out/docs/kotlin
	java -jar lib/dokka.jar src -output out/docs/kotlin -classpath $(cp)
	java -cp $(JAVA_HOME)/lib/tools.jar$(SEP)lib/dokka.jar org.jetbrains.dokka.MainKt src -output out/docs/java -format javadoc -classpath $(cp)

pubdocs:
	rm -rf pages/*
	cp -R out/docs/java pages/java
	cp -R out/docs/kotlin pages/kotlin
	cd pages ; git add -A . ; git commit -m "update" ; git push

publish:
	curl -T out/violin-$(VERSION).jar -u$(BINTRAY_USER):$(BINTRAY_API_KEY) \
	https://api.bintray.com/content/norswap/maven/violin/$(VERSION)/violin-$(VERSION).jar;publish=1
	echo "\n"

cleanlibs:
	rm -rf lib

.PHONY: \
  build \
  buildtests \
  test \
  refresh \
  clean \
  jar \
  docs \
  publish


.SILENT:

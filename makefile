VERSION:=0.1.0
KOTLIN_VER:=1.0.3
TESTNG_VER:=6.9.10
JCOMMA_VER:=1.48
DOKKA_VER:=0.9.8

TESTNG:="http://central.maven.org/maven2/org/testng/testng/$(TESTNG_VER)/testng-$(TESTNG_VER).jar"
JCOMMA:="http://central.maven.org/maven2/com/beust/jcommander/$(JCOMMA_VER)/jcommander-$(JCOMMA_VER).jar"
DOKKA:="https://github.com/Kotlin/dokka/releases/download/$(DOKKA_VER)/dokka-fatjar.jar"
KOTLIN:="https://github.com/JetBrains/kotlin/releases/download/v$(KOTLIN_VER)/kotlin-compiler-$(KOTLIN_VER).zip"

ifeq ($(shell if [ -d kotlinc ]; then echo "yes"; fi),yes)
	KOTLINC:=kotlinc/bin/kotlinc
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
basecp:=out/production$(SEP)$(jars)
cp:="$(basecp)"
testcp:="$(basecp)$(SEP)out/test"

build:
	mkdir -p out/production
	$(KOTLINC) -cp $(cp) src -d out/production

buildtests:
	mkdir -p out/test
	$(KOTLINC) -cp $(testcp) test -d out/test

test:
	java -cp $(testcp) org.testng.TestNG test/testng.xml -d out/test-output

rebuild: build buildtests test

clean:
	rm -rf out

kotlin:
	curl -L $(KOTLIN) > compiler.zip
	unzip compiler.zip -d .
	rm compiler.zip

cleankotlin:
	rm -rf kotlinc

deps:
	mkdir -p lib
	curl -L $(DOKKA)	  > lib/dokka.jar
	curl -L $(TESTNG)	  > lib/testng.jar
	curl -L $(JCOMMA)     > lib/jcommander.jar

cleandeps:
	rm -rf lib

jar:
	find out -name .DS_Store -type f -delete
	jar cf out/violin-$(VERSION).jar -C out/production .

jars: jar
	find src -name .DS_Store -type f -delete
	jar cf out/violin-$(VERSION)-sources.jar -C src .
	jar cf out/violin-$(VERSION)-javadoc.jar -C out/docs/java .
	jar cf out/violin-$(VERSION)-kdoc.jar -C out/docs/kotlin .

BINTRAY_PATH:=https://api.bintray.com/content/norswap/maven/violin/$(VERSION)
BINTRAY_PATH:=$(BINTRAY_PATH)/norswap/violin/$(VERSION)

binup = curl -T out/violin-$(VERSION)$(1) -u$(BINTRAY_USER):$(BINTRAY_API_KEY) \
		"$(BINTRAY_PATH)/violin-$(VERSION)$(1);publish=1;override=1" ; echo "\n"

publish:
	sed "s/VERSION/$(VERSION)/g" violin.pom > out/violin-$(VERSION).pom
	$(call binup,.pom)
	$(call binup,.jar)
	$(call binup,-sources.jar)
	$(call binup,-javadoc.jar)
	$(call binup,-kdoc.jar)

docs:
	mkdir -p out/docs/java
	mkdir -p out/docs/kotlin
	java -jar lib/dokka.jar src -output out/docs/kotlin -classpath $(cp) \
		-include src/norswap/violin/stream/package.md
	java -cp $(JAVA_HOME)/lib/tools.jar$(SEP)lib/dokka.jar org.jetbrains.dokka.MainKt src \
		 -output out/docs/java -format javadoc -classpath $(cp)

pubdocs:
	rm -rf pages/*
	cp -R out/docs/java pages/java
	cp -R out/docs/kotlin pages/kotlin
	cd pages ; git add -A . ; git commit -m "update" ; git push

.PHONY: \
  build \
  buildtests \
  test \
  rebuild \
  clean \
  kotlin \
  cleankotlin \
  deps \
  cleandeps \
  jar \
  publish \
  docs \
  pubdocs

.SILENT:

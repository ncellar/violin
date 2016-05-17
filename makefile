TESTNG:="http://central.maven.org/maven2/org/testng/testng/6.9.10/testng-6.9.10.jar"
KOTLIN_RT:="http://mvnrepository.com/artifact/org.jetbrains.kotlin/kotlin-runtime/1.0.2"
KOTLIN_RE:="http://central.maven.org/maven2/org/jetbrains/kotlin/kotlin-reflect/1.0.2/kotlin-reflect-1.0.2.jar"

ifeq ($(OS),Windows_NT)
	SEP:=;
else
	SEP:=:
endif

space:=$(eval) $(eval)
# java supports "lib/*", but not kotlin
jars:=$(subst $(space),$(SEP),$(shell find lib -name *.jar))
classpath:="out$(SEP)out/resources$(SEP)$(jars)"

build:
	mkdir -p out/resources
	if [ -d resources ]; then cp -R resources/. output; fi
	kotlinc -no-stdlib -cp $(classpath) src test -d out

clean:
	rm -rf out

fetch:
	mkdir -p lib
	curl $(TESTNG)	  > lib/testng.jar
	curl $(KOTLIN_RT) > lib/kotlin-runtime.jar
	curl $(KOTLIN_RE) > lib/kotlin-reflect.jar
	git rev-parse HEAD > last_fetch_commit.txt

.PHONY: \
  fetch

.SILENT:

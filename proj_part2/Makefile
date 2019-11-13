# Update these variables to match the locations
JUNIT_JAR="/Applications/Eclipse.app/Contents/Eclipse/plugins/org.junit_4.12.0.v201504281640/junit.jar"
HAMCREST_JAR="/Applications/Eclipse.app/Contents/Eclipse/plugins/org.hamcrest.core_1.3.0.v20180420-1519.jar"
ANTLR_JAR=/Users/dianasok/Desktop/CS152/antlr-4.7.2-complete.jar
# These variables should not need to be changed
GRAMMAR_NAME=FeatherweightJavaScript
GRAMMAR=${GRAMMAR_NAME}.g4
TEST_CLASSPATH=${JUNIT_JAR}:${HAMCREST_JAR}:${ANTLR_JAR}
FWJS_SCRIPT_DIR=fwjsScripts
SCRIPTS=closure.fwjs examples.fwjs functions.fwjs operators.fwjs test.fwjs \
				controlStructs.fwjs factorial.fwjs lists.fwjs scoping.fwjs while.fwjs
TREES_DIR=parseTrees
# Choosing build instead of bin to avoid conflicts with Eclipse
BUILD_DIR=build
SRC_FOLDERS=edu/sjsu/fwjs
PACKAGE_NAME=edu.sjsu.fwjs
GEN_SRC_BASE_DIR=generatedSrc
PARSER_SRC_FOLDERS=edu/sjsu/fwjs/parser
GEN_SRC_DIR=${GEN_SRC_BASE_DIR}/${PARSER_SRC_FOLDERS}
PARSER_PACKAGE_NAME=edu.sjsu.fwjs.parser
ZIP_FILE=solution.zip

.PHONY: all test run clean spotless generate
all: generate
	mkdir -p ${BUILD_DIR}/${SRC_FOLDERS}
	javac -cp ${TEST_CLASSPATH} -d ${BUILD_DIR} src/${SRC_FOLDERS}/*.java testSrc/${SRC_FOLDERS}/*.java ${GEN_SRC_DIR}/*.java

generate: ${GRAMMAR}
	mkdir -p ${GEN_SRC_DIR}
	java -jar ${ANTLR_JAR} -no-listener -visitor ${GRAMMAR} -o ${GEN_SRC_DIR}

parse:
	mkdir -p ${TREES_DIR}
	$(foreach script, ${SCRIPTS}, java -cp ${BUILD_DIR}:${ANTLR_JAR} org.antlr.v4.runtime.misc.TestRig \
		${PARSER_PACKAGE_NAME}.${GRAMMAR_NAME} prog -gui ${FWJS_SCRIPT_DIR}/${script} > ${TREES_DIR}/${script}.tree;)

test:
	java -cp ${BUILD_DIR}:${TEST_CLASSPATH} org.junit.runner.JUnitCore ${PACKAGE_NAME}.ExpressionTest

run:
	java -cp ${BUILD_DIR} ${PACKAGE_NAME}.Interpreter

${ZIP_FILE}:
	zip ${ZIP_FILE} src/${SRC_FOLDERS}/*.java ${GRAMMAR}

clean:
	-rm -r ${BUILD_DIR}

spotless: clean
	-rm ${ZIP_FILE}
	-rm -r ${GEN_SRC_BASE_DIR}
	-rm -r ${TREES_DIR}


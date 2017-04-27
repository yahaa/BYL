cmake_minimum_required(VERSION 3.7)
project(exp3)

set(CMAKE_CXX_STANDARD 11)

set(SOURCE_FILES main.cpp Analyser.h Glossary.h LR0Analyser.h)
add_executable(exp3 ${SOURCE_FILES})
cmake_minimum_required(VERSION 3.8)

find_package(Qt5 COMPONENTS Core Widgets REQUIRED)

set(CMAKE_CXX_STANDARD 14)

set(SOURCES src/BabyStepTimer.cpp)

add_executable(babysteptimer ${SOURCES})
target_link_libraries(babysteptimer PRIVATE Qt5::Widgets)

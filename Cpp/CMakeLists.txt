cmake_minimum_required(VERSION 3.7)

find_package(Qt5 COMPONENTS Core Widgets Multimedia REQUIRED)
find_package(Threads)

set(CMAKE_CXX_STANDARD 11)
set(CMAKE_AUTOMOC ON)

set(SOURCES
  src/BabystepsTimer.cpp
  src/BabystepsTimer.h
  src/main.cpp
  )

add_executable(babysteptimer ${SOURCES})
target_link_libraries(babysteptimer
  PRIVATE Qt5::Widgets
  PRIVATE Qt5::Multimedia
  PRIVATE ${CMAKE_THREAD_LIBS_INIT}
  )

configure_file(../CSharp/BabyStepTimer/Resources/2166__suburban-grilla__bowl-struck.wav ${CMAKE_CURRENT_BINARY_DIR} COPYONLY)
configure_file(../CSharp/BabyStepTimer/Resources/32304__acclivity__shipsbell.wav ${CMAKE_CURRENT_BINARY_DIR} COPYONLY)

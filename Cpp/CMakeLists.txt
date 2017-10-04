cmake_minimum_required(VERSION 3.7)

find_package(Qt5 COMPONENTS Core Widgets Multimedia REQUIRED)
find_package(Threads)

set(CMAKE_CXX_STANDARD 11)
set(CMAKE_AUTOMOC ON)

set(SOURCES
  src/BabystepsTimer.cpp
  src/BabystepsTimer.h
  )

add_library(babystepstimer-lib ${SOURCES})
target_link_libraries(babystepstimer-lib
  PUBLIC Qt5::Widgets
  PUBLIC Qt5::Multimedia
  PRIVATE ${CMAKE_THREAD_LIBS_INIT}
  )

add_executable(babystepstimer src/main.cpp)
target_link_libraries(babystepstimer
  PRIVATE babystepstimer-lib
  )

configure_file(../CSharp/BabyStepTimer/Resources/2166__suburban-grilla__bowl-struck.wav ${CMAKE_CURRENT_BINARY_DIR} COPYONLY)
configure_file(../CSharp/BabyStepTimer/Resources/32304__acclivity__shipsbell.wav ${CMAKE_CURRENT_BINARY_DIR} COPYONLY)


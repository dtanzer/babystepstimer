cmake_minimum_required(VERSION 3.8)

find_package(Qt5 COMPONENTS Core Widgets REQUIRED)
find_package(Threads)

set(CMAKE_CXX_STANDARD 14)
set(CMAKE_AUTOMOC ON)

set(SOURCES
  src/BabystepsTimer.cpp
  src/BabystepsTimer.h
  )

add_executable(babysteptimer ${SOURCES})
target_link_libraries(babysteptimer
  PRIVATE Qt5::Widgets
  PRIVATE ${CMAKE_THREAD_LIBS_INIT}
  )

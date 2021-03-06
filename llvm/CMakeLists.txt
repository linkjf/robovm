cmake_minimum_required(VERSION 2.8)

include(ExternalProject)

if(OS STREQUAL "linux")
  set(LINUX YES)
elseif(OS STREQUAL "macosx")
  set(MACOSX YES)
elseif(OS STREQUAL "windows")
  set(WINDOWS YES)
endif()

if (WINDOWS)
  # set(CMAKE_CROSSCOMPILING TRUE) 
  set(CMAKE_SYSTEM_NAME Windows)

  #should be passed from build.sh, just here to prevent warning
  set(CMAKE_RC_COMPILER "/usr/bin/x86_64-w64-mingw32-windres")
  
  #these are not actually used by robovm but find_package does not always resolve them, use JAVA_HOME env var to prevent paths?
  set(JAVA_AWT_LIBRARY "/usr/lib/jvm/java-7-openjdk-amd64/jre/lib/amd64/libawt.so")
  set(JAVA_JVM_LIBRARY "/usr/lib/jvm/java-7-openjdk-amd64/jre/lib/amd64/server/libjvm.so")

  # adjust the default behaviour of the FIND_XXX() commands:
  # search headers and libraries in the target environment, search
  # programs in the host environment
  set(CMAKE_FIND_ROOT_PATH_MODE_PROGRAM NEVER)
  set(CMAKE_FIND_ROOT_PATH_MODE_LIBRARY ONLY)
  set(CMAKE_FIND_ROOT_PATH_MODE_INCLUDE ONLY)
endif()

project(robovm-llvm)

find_package(JNI REQUIRED)

message (STATUS "JNI_INCLUDE_DIRS=${JNI_INCLUDE_DIRS}")
message (STATUS "JNI_LIBRARIES=${JNI_LIBRARIES}")

if (ARCH STREQUAL "x86")
  set(X86 YES)
elseif (ARCH STREQUAL "x86_64")
  set(X86_64 YES)
endif()

set(C_CXX_FLAGS "${C_CXX_FLAGS} -fvisibility=hidden -fdata-sections -ffunction-sections")
if(MACOSX AND X86)
  set(C_CXX_FLAGS "${C_CXX_FLAGS} -arch i386 -mmacosx-version-min=10.6")
  set(LD_FLAGS "${LD_FLAGS} -arch i386 -mmacosx-version-min=10.6")
elseif(MACOSX AND X86_64)
  set(C_CXX_FLAGS "${C_CXX_FLAGS} -arch x86_64 -mmacosx-version-min=10.6")
  set(LD_FLAGS "${LD_FLAGS} -arch x86_64 -mmacosx-version-min=10.6")
elseif(LINUX AND X86)
  set(C_CXX_FLAGS "${C_CXX_FLAGS} -m32")
  set(LD_FLAGS "${LD_FLAGS} -m64")
elseif(LINUX AND X86_64)
  set(C_CXX_FLAGS "${C_CXX_FLAGS} -m64")
  set(LD_FLAGS "${LD_FLAGS} -m64")
elseif(WINDOWS AND X86)
  set(C_CXX_FLAGS "${C_CXX_FLAGS} -m32")
  set(LD_FLAGS "${LD_FLAGS} -m32")
elseif(WINDOWS AND X86_64)
  set(C_CXX_FLAGS "${C_CXX_FLAGS} -m64")
  set(LD_FLAGS "${LD_FLAGS} -m64")
endif()

if(MACOSX)
  exec_program(xcode-select ARGS --print-path OUTPUT_VARIABLE XCODE_PATH)
  set(SYSROOT "${XCODE_PATH}/Platforms/MacOSX.platform/Developer/SDKs/MacOSX10.9.sdk")
  if(NOT IS_DIRECTORY ${SYSROOT})
    set(SYSROOT "${XCODE_PATH}/Platforms/MacOSX.platform/Developer/SDKs/MacOSX10.8.sdk")
  endif()
  if(NOT IS_DIRECTORY ${SYSROOT})
    message(FATAL_ERROR "No supported MacOSX SDK found")
  endif()
  set(C_CXX_FLAGS "${C_CXX_FLAGS} -isysroot ${SYSROOT}")
endif()

set(LLVM_ARCHS "X86^^ARM")
set(LLVM_C_FLAGS "${C_CXX_FLAGS} -fvisibility=hidden -fdata-sections -ffunction-sections")
set(LLVM_CXX_FLAGS "${C_CXX_FLAGS} -fvisibility=hidden -fdata-sections -ffunction-sections")
ExternalProject_Add(extllvm
  URL http://llvm.org/releases/3.4/llvm-3.4.src.tar.gz
  LIST_SEPARATOR ^^
  CONFIGURE_COMMAND cmake -DCMAKE_CXX_COMPILER=${CMAKE_CXX_COMPILER} -DCMAKE_C_COMPILER=${CMAKE_C_COMPILER} -DCMAKE_RC_COMPILER=${CMAKE_RC_COMPILER} -DCMAKE_SYSTEM_NAME=${CMAKE_SYSTEM_NAME} -DCMAKE_BUILD_TYPE=Release -DBUILD_SHARED_LIBS=OFF "-DCMAKE_INSTALL_PREFIX=${CMAKE_BINARY_DIR}/llvm" "-DLLVM_TARGETS_TO_BUILD=${LLVM_ARCHS}" "-DCMAKE_C_FLAGS=${LLVM_C_FLAGS}" "-DCMAKE_CXX_FLAGS=${LLVM_CXX_FLAGS}" ../extllvm/
)

set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS} ${C_CXX_FLAGS}")
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} ${C_CXX_FLAGS} -fno-rtti")
set(CMAKE_INSTALL_DO_STRIP YES)

add_definitions(-D__STDC_LIMIT_MACROS -D__STDC_CONSTANT_MACROS)
include_directories(${JAVA_INCLUDE_PATH} ${JAVA_INCLUDE_PATH2} ${CMAKE_BINARY_DIR}/llvm/include)

# This was generated using 'llvm-config --libs core bitreader bitwriter irreader ipo arm x86 OUTPUT_VARIABLE LLVM_LIBS'
set(LLVM_LIBS
  -lLLVMX86Disassembler -lLLVMX86AsmParser -lLLVMX86CodeGen -lLLVMX86Desc -lLLVMX86Info 
  -lLLVMX86AsmPrinter -lLLVMX86Utils -lLLVMARMDisassembler -lLLVMARMCodeGen -lLLVMSelectionDAG 
  -lLLVMAsmPrinter -lLLVMCodeGen -lLLVMARMAsmParser -lLLVMMCParser -lLLVMARMDesc -lLLVMARMInfo 
  -lLLVMARMAsmPrinter -lLLVMipo -lLLVMObjCARCOpts -lLLVMVectorize -lLLVMScalarOpts -lLLVMInstCombine 
  -lLLVMTransformUtils -lLLVMipa -lLLVMAnalysis -lLLVMTarget -lLLVMMC -lLLVMObject -lLLVMIRReader 
  -lLLVMAsmParser -lLLVMBitWriter -lLLVMBitReader -lLLVMCore -lLLVMSupport
)
# This was generated using 'llvm-config --ldflags'
if(LINUX)
  set(LLVM_LDFLAGS
    -L"${CMAKE_BINARY_DIR}/llvm/lib"
    -lrt -ldl -lpthread -lz
  )
elseif(WINDOWS)
  set(LLVM_LDFLAGS
    -L"${CMAKE_BINARY_DIR}/llvm/lib"
    -ldbghelp -static-libgcc -static-libstdc++
  )
elseif(MACOSX)
  set(LLVM_LDFLAGS
    -L"${CMAKE_BINARY_DIR}/llvm/lib"
    -lcurses -lpthread -lz
  )
endif()

set(SRC
  src/main/native/LLVM_wrap.c
  src/main/native/LLVMExtra.cpp
)

add_library(robovm-llvm SHARED ${SRC})
add_dependencies(robovm-llvm extllvm)
target_link_libraries(robovm-llvm ${LLVM_LIBS} ${LLVM_LDFLAGS})
set_target_properties(robovm-llvm PROPERTIES LINKER_LANGUAGE CXX)
if(LINUX)
  set_target_properties(robovm-llvm PROPERTIES LINK_FLAGS "-Wl,--gc-sections -Wl,--version-script=${CMAKE_SOURCE_DIR}/src/main/native/exports_linux.sym")
elseif(MACOSX)
  set_target_properties(robovm-llvm PROPERTIES LINK_FLAGS "-Wl,-dead_strip -Wl,-exported_symbols_list -Wl,${CMAKE_SOURCE_DIR}/src/main/native/exports_macosx.sym")
endif()
set(INSTALL_DIR ${CMAKE_SOURCE_DIR}/src/main/resources/org/robovm/llvm/binding/${OS}/${ARCH})
if(WINDOWS)
  install(TARGETS robovm-llvm RUNTIME DESTINATION ${INSTALL_DIR})
else()
  install(TARGETS robovm-llvm LIBRARY DESTINATION ${INSTALL_DIR})
endif()

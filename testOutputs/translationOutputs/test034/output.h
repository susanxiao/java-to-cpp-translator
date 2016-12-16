#pragma once
#include <iostream>
#include "java_lang.h"

using namespace java::lang;

namespace inputs {
	namespace test034 {
		struct __A;
		struct __A_VT;


		typedef __A* A;

		struct __A {
			__A_VT* __vptr;

			__A();

			static Class __class();

			static __A_VT __vtable;

			static int32_t methodMByte(A, uint8_t);
			static int32_t methodMInt(A, int32_t);
			static void methodMDouble(A, double);
		};

		struct __A_VT {
			Class __isa;

			int32_t (*hashCode)(A);
			bool (*equals)(A, Object);
			Class (*getClass)(A);
			String (*toString)(A);
			int32_t (*methodMByte)(A, uint8_t);
			int32_t (*methodMInt)(A, int32_t);
			void (*methodMDouble)(A, double);

			__A_VT()
			: __isa(__A::__class()),
			hashCode((int32_t(*)(A))&__Object::hashCode),
			equals((bool(*)(A, Object))&__Object::equals),
			getClass((Class(*)(A))&__Object::getClass),
			toString((String(*)(A))&__Object::toString),
			methodMByte(&__A::methodMByte),
			methodMInt(&__A::methodMInt),
			methodMDouble(&__A::methodMDouble)
			{}
		};

	}
}

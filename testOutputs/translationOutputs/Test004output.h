#pragma once
#include <iostream>
#include "java_lang.h"

using namespace java::lang;

namespace inputs {
	namespace test004 {


	struct __A;
	struct __A_VT;

	typedef __A* A;

	struct __A
	{

	String fld;

	__A_VT* __vptr;

	__A(String fld);

	static Class __class();

	static int32_t hashCode(A);

	static __A_VT __vtable;

	static String getFld(A);

	};

	struct __A_VT
	{

	Class __isa;
	String (*getFld)(A);
	int32_t (*hashCode)(A);
	Class (*getClass)(A);
	bool (*equals)(A,Object);

	__A_VT()
	: __isa(__A::__class()),
		getFld(&__A::getFld),
		hashCode(&__A::hashCode),
		getClass((Class(*)(A)) &__Object::getClass),
		equals((bool(*)(A,Object)) &__Object::equals)
		{}

	};



	}
}

